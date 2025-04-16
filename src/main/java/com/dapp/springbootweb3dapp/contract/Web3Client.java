package com.dapp.springbootweb3dapp.contract;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
public class Web3Client {

    private static final Logger log = LoggerFactory.getLogger(Web3Client.class);
    private static Web3Client client;

    private final Web3j web3j;

    // 0x4dA59CcE7f395f17bCFFee88DA1f1A8c3897441D
    // 1, 筛选日志。 2，。从收据读
    public static int log_read_type = 2;

    public static String contract_address = "0xd7C1B77D89595a9Ab5C344c2fcc984b7A3090DD3";
    public static String admin_address_key = "0x3437c5a56bd5b0e2af3bbf54c60ba58883325c235936c2238d7ec909d0d7087b";

    private Web3Client() {
        web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.bnbchain.org:8545"));
    }

    public static Web3j getInstance() {
        if (client == null) {
            client = new Web3Client();
        }
        return client.web3j;
    }

    @PreDestroy
    public void close() {
        if (web3j != null) {
            web3j.shutdown();
        }
    }

    public static void main(String[] args) throws IOException {
        Web3j web3 = getInstance();


        DefaultBlockParameter blockNumber = DefaultBlockParameter.valueOf(BigInteger.valueOf(48128705));
        Request<?, EthBlock> request = web3.ethGetBlockByNumber(blockNumber, true);
        EthBlock ethBlock = request.send();
        for (EthBlock.TransactionResult transaction : ethBlock.getBlock().getTransactions()) {
            EthBlock.TransactionObject object = (EthBlock.TransactionObject) transaction;
            String to = object.getTo();

            if (StrUtil.equals(to, "0xd7C1B77D89595a9Ab5C344c2fcc984b7A3090DD3".toLowerCase())) {
                String hash = object.getHash();

                EthGetTransactionReceipt transactionReceipt = web3.ethGetTransactionReceipt(hash).send();
                List<Log> logs = transactionReceipt.getResult().getLogs();
                System.out.println(logs);
            }
        }


//        System.out.println(logs);
    }
}
