package com.dapp.springbootweb3dapp.contract;


import com.dapp.springbootweb3dapp.access.api.ProBlockApi;
import com.dapp.springbootweb3dapp.access.api.ProProductApi;
import com.dapp.springbootweb3dapp.access.api.ProProductCycleApi;
import com.dapp.springbootweb3dapp.access.entity.ProBlock;
import com.dapp.springbootweb3dapp.access.entity.ProProduct;
import com.dapp.springbootweb3dapp.access.entity.ProProductCycle;
import com.dapp.springbootweb3dapp.access.state.StateProProductCycle;
import com.dapp.springbootweb3dapp.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.StaticGasProvider;
import sol.Main;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TaskOpenDraw implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(TaskOpenDraw.class);
    private final List<Long> numbers;         // 需要处理的区块号列表
    private final List<String> contract_addresss; // 关联的智能合约地址列表

    public TaskOpenDraw(List<Long> numbers, List<String> contract_addresss) {
        this.numbers = numbers;
        this.contract_addresss = contract_addresss;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        // 获取Spring管理的DAO实例
        ProProductCycleApi proProductCycleApi = SpringUtils.getBean(ProProductCycleApi.class);
        ProBlockApi proBlockApi = SpringUtils.getBean(ProBlockApi.class);
        ProProductApi proProductApi = SpringUtils.getBean(ProProductApi.class);

        for (Long number : numbers) {
            // 1. 获取待开奖的产品周期 --------------------------
            List<ProProductCycle> cycles = proProductCycleApi.listByPreOpenDrawBlockNumber(number);

            for (ProProductCycle cycle : cycles) {
                // 2. 状态校验 -------------------------------
                // 跳过非等待开奖状态的周期
                if (cycle.getState().intValue() != StateProProductCycle.wait.getState()) {
                    continue;
                }
                // 3. 获取关联产品信息 --------------------------

                ProProduct product = proProductApi.getSingle(cycle.getContractAddress(), cycle.getTokenAddress(), cycle.getProductValue());
                if (product == null) {
                    continue;
                }
                // 4. 计算随机数种子 ---------------------------
                // 获取区块范围：创建区块 ~ 预开奖区块
                // 执行开奖逻辑
                List<ProBlock> blocks = proBlockApi.listByNumbers(cycle.getCreateBlockNumber(), cycle.getPreOpenDrawBlockNumber().subtract(cycle.getCreateBlockNumber()));

                BigDecimal total = new BigDecimal(0);

                List<Main.BlockVo> vos = new ArrayList<>();
                // 累加区块特征值
                for (ProBlock block : blocks) {
                    BigDecimal temp_row = new BigDecimal(block.getNumber()).multiply(new BigDecimal(block.getTransactionCount()));
                    BigDecimal row = temp_row.add(new BigDecimal(block.getTimestamp()));
                    total = total.add(row);
                    vos.add(new Main.BlockVo(block.getNumber(), block.getTransactionCount(), block.getTimestamp()));
                }

                // 5. 生成最终开奖码 ----------------------------
                BigDecimal remainder = total.remainder(new BigDecimal(12));
                BigDecimal lock_code = new BigDecimal(product.getStartLockCode()).add(remainder);
                log.info("区块随机和: {}", total);
                log.info("区块求与数： {}", remainder);
                log.info("最终开奖号码: {}", lock_code);
                // 6. 准备区块链交互 ----------------------------
                // 使用管理员私钥创建凭证（需确保密钥安全）
//                String key = "0x3437c5a56bd5b0e2af3bbf54c60ba58883325c235936c2238d7ec909d0d7087b";
                Credentials credentials = Credentials.create(Web3Client.admin_address_key);


                Web3j web3j = Web3Client.getInstance();
                try {
                    // 7. Gas费用估算 --------------------------
                    BigInteger gasLimit = BigInteger.valueOf(0);
                    EthGasPrice gasPrice = web3j.ethGasPrice().send();


                    StaticGasProvider gasProvider = new StaticGasProvider(gasPrice.getGasPrice(), gasLimit);
                    // 8. 加载智能合约 ------------------------
                    Main main = Main.load(product.getContractAddress(), Web3Client.getInstance(), credentials, gasProvider);
                    // 9. 构建交易调用 ------------------------
                    RemoteFunctionCall<TransactionReceipt> transactionReceiptRemoteFunctionCall = main.openDraw(cycle.getTokenAddress(), cycle.getProductValue(), cycle.getCurrentIndex(), vos);
                    String encoded = transactionReceiptRemoteFunctionCall.encodeFunctionCall();


                    EthEstimateGas estimateGas = web3j.ethEstimateGas(Transaction.createEthCallTransaction(credentials.getAddress(), cycle.getContractAddress(), encoded)).send();
                    main.setGasProvider(new StaticGasProvider(gasPrice.getGasPrice(), estimateGas.getAmountUsed()));


                    TransactionReceipt transactionReceipt = transactionReceiptRemoteFunctionCall.send();
                    log.info("开奖收据 {}", transactionReceipt);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
