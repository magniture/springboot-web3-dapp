package com.dapp.springbootweb3dapp.access.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
public class TransactionService {

    public void executeWithTransaction() {
        log.info("当前方法事物状态：{}", TransactionSynchronizationManager.isActualTransactionActive());
    }

}
