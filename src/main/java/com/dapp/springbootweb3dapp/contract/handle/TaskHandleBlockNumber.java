package com.dapp.springbootweb3dapp.contract.handle;

import cn.hutool.core.util.StrUtil;
import com.dapp.springbootweb3dapp.access.api.ProBlockApi;
import com.dapp.springbootweb3dapp.access.entity.ProBlock;
import com.dapp.springbootweb3dapp.access.state.StateProBlock;
import com.dapp.springbootweb3dapp.contract.TaskParseLog;
import com.dapp.springbootweb3dapp.contract.Web3Client;
import com.dapp.springbootweb3dapp.utils.spring.SpringUtils;
import com.dapp.springbootweb3dapp.utils.thread.ThreadPoolConfig;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskHandleBlockNumber implements Runnable {
    // 需要处理的区块号列表
    private final List<Long> numbers;
    // 需要监听的智能合约地址列表
    private final List<String> contract_addresss;

    public TaskHandleBlockNumber(List<Long> numbers, List<String> contract_addresss) {
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
        // 获取Web3j客户端实例（区块链连接）
        Web3j web3j = Web3Client.getInstance();
        // 获取线程池（用于并发处理交易）
        ThreadPoolExecutor executor = ThreadPoolConfig.getTransactionNewPool();

        // 获取区块数据访问接口（Spring管理的Bean）
        ProBlockApi proBlockApi = SpringUtils.getBean(ProBlockApi.class);
        // 遍历需要处理的区块号
        for (Long number : numbers) {
            // 1. 区块数据预处理 ---------------------------------
            // 检查数据库是否已存在该区块记录
            ProBlock proBlock = proBlockApi.getByBlockNumber(number);
            if (proBlock == null) {
                // 新建区块记录
                proBlock = new ProBlock();
                try {
                    // 从区块链节点获取区块详细信息
                    EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(number)), true).send();
                    // 填充区块基础信息
                    proBlock.setNumber(ethBlock.getBlock().getNumber());
                    proBlock.setHash(ethBlock.getBlock().getHash());
                    proBlock.setTimestamp(ethBlock.getBlock().getTimestamp());
                    proBlock.setTransactionCount(BigInteger.valueOf(ethBlock.getBlock().getTransactions().size()));
                    proBlock.setReadTime(LocalDateTime.now());
                    proBlock.setState(StateProBlock.error.getState()); // 初始状态设为错误
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;// 出现异常跳过当前区块
                }
                proBlockApi.save(proBlock); // 保存到数据库
            }


            try {
                // 2. 日志获取逻辑 ---------------------------------

                List<Log> eth_logs = new ArrayList<>();

                DefaultBlockParameter blockNumber = DefaultBlockParameter.valueOf(BigInteger.valueOf(number));
                // 根据配置选择日志获取方式
                if (Web3Client.log_read_type == 1) {
                    // 方式1：直接通过ethGetLogs接口获取日志（推荐）
                    EthFilter filter = new EthFilter(blockNumber, blockNumber, contract_addresss);
                    EthLog ethLog = web3j.ethGetLogs(filter).send();
                    for (EthLog.LogResult result : ethLog.getLogs()) {
                        Log eth_log = (Log) result;
                        eth_logs.add(eth_log);
                    }
                } else {
                    // 方式2：遍历区块中的交易获取日志（兼容性方式）

                    Request<?, EthBlock> request = web3j.ethGetBlockByNumber(blockNumber, true);
                    EthBlock ethBlock = request.send();
                    for (EthBlock.TransactionResult transaction : ethBlock.getBlock().getTransactions()) {
                        EthBlock.TransactionObject object = (EthBlock.TransactionObject) transaction;
                        String to = object.getTo();
                        // 只处理目标地址为指定合约的交易

                        if (StrUtil.equals(to, Web3Client.contract_address.toLowerCase())) {
                            String hash = object.getHash();
                            // 获取交易收据中的日志

                            EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(hash).send();
                            List<Log> logs = transactionReceipt.getResult().getLogs();
                            eth_logs.addAll(logs);
                        }
                    }
                }

                // 3. 日志处理逻辑 ---------------------------------
                if (eth_logs.isEmpty()) {
                    // 无日志时标记区块状态为完成

                    proBlock.setState(StateProBlock.ok.getState());
                    proBlock.setOkTime(LocalDateTime.now());
                    proBlockApi.saveOrUpdate(proBlock);
                    continue;
                }
                // 遍历所有日志进行异步处理

                for (Log eth_log : eth_logs) {
                    // 提取事件ID（第一个topic）

//                    Log eth_log = (Log) result;
                    List<String> topics = eth_log.getTopics();
                    String id = topics.get(0);

                    CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
                        try {
                            new TaskParseLog(id, eth_log).run();
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }, executor);

                    Boolean aBoolean = completableFuture.join();
                    if (aBoolean) {
                        proBlock.setState(StateProBlock.ok.getState());
                        proBlock.setOkTime(LocalDateTime.now());
                    } else {
                        proBlock.setState(StateProBlock.error.getState());
                        proBlock.setOkTime(LocalDateTime.now());
                    }

                    proBlockApi.saveOrUpdate(proBlock);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
