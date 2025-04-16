package com.dapp.springbootweb3dapp.contract.handle;

import cn.hutool.core.util.StrUtil;
import com.sunyoki.seeker.access.api.ProBlockApi;
import com.sunyoki.seeker.access.entity.ProBlock;
import com.sunyoki.seeker.access.state.StateProBlock;
import com.sunyoki.seeker.contract.TaskParseLog;
import com.sunyoki.seeker.contract.Web3Client;
import com.sunyoki.seeker.utils.spring.SpringUtils;
import com.sunyoki.seeker.utils.thread.ThreadPoolConfig;
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

    private final List<Long> numbers;

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
        Web3j web3j = Web3Client.getInstance();
        ThreadPoolExecutor executor = ThreadPoolConfig.getTransactionNewPool();


        ProBlockApi proBlockApi = SpringUtils.getBean(ProBlockApi.class);

        for (Long number : numbers) {
            ProBlock proBlock = proBlockApi.getByBlockNumber(number);
            if (proBlock == null) {
                proBlock = new ProBlock();
                try {
                    EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(number)), true).send();
                    proBlock.setNumber(ethBlock.getBlock().getNumber());
                    proBlock.setHash(ethBlock.getBlock().getHash());
                    proBlock.setTimestamp(ethBlock.getBlock().getTimestamp());
                    proBlock.setTransactionCount(BigInteger.valueOf(ethBlock.getBlock().getTransactions().size()));
                    proBlock.setReadTime(LocalDateTime.now());
                    proBlock.setState(StateProBlock.error.getState());
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                proBlockApi.save(proBlock);
            }


            try {
                List<Log> eth_logs = new ArrayList<>();

                DefaultBlockParameter blockNumber = DefaultBlockParameter.valueOf(BigInteger.valueOf(number));
                if (Web3Client.log_read_type == 1) {
                    EthFilter filter = new EthFilter(blockNumber, blockNumber, contract_addresss);
                    EthLog ethLog = web3j.ethGetLogs(filter).send();
                    for (EthLog.LogResult result : ethLog.getLogs()) {
                        Log eth_log = (Log) result;
                        eth_logs.add(eth_log);
                    }
                } else {
                    Request<?, EthBlock> request = web3j.ethGetBlockByNumber(blockNumber, true);
                    EthBlock ethBlock = request.send();
                    for (EthBlock.TransactionResult transaction : ethBlock.getBlock().getTransactions()) {
                        EthBlock.TransactionObject object = (EthBlock.TransactionObject) transaction;
                        String to = object.getTo();
                        if (StrUtil.equals(to, Web3Client.contract_address.toLowerCase())) {
                            String hash = object.getHash();
                            EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(hash).send();
                            List<Log> logs = transactionReceipt.getResult().getLogs();
                            eth_logs.addAll(logs);
                        }
                    }
                }


                if (eth_logs.isEmpty()) {
                    proBlock.setState(StateProBlock.ok.getState());
                    proBlock.setOkTime(LocalDateTime.now());
                    proBlockApi.saveOrUpdate(proBlock);
                    continue;
                }

                for (Log eth_log : eth_logs) {
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
