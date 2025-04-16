package com.dapp.springbootweb3dapp.contract;

import com.sunyoki.seeker.access.api.ProBlockApi;
import com.sunyoki.seeker.access.entity.ProBlock;
import com.sunyoki.seeker.contract.handle.TaskHandleBlockNumber;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlockNumber;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TaskWeb3 {

    @Resource
    ProBlockApi proBlockApi;


    public static boolean is_run = false;

    @XxlJob("DiscoveryBlock")
    public void DiscoveryBlock() {
        if (is_run) {
            log.error("还有任务没有完成。。。。");
            return;
        }
        is_run = true;

        log.info("任务开始");
        // 得到数据库里面最后一个
        ProBlock last_block = proBlockApi.last();

        BigInteger last_blockNumber = last_block.getNumber();

        try {
            EthBlockNumber server_blockNumber = Web3Client.getInstance().ethBlockNumber().send();
            log.info("服务器区块号{}", server_blockNumber.getBlockNumber());
            log.info("本地区块号 {}", last_blockNumber);

            List<Long> numbers = new ArrayList<>();
            for (int i = 0; i < server_blockNumber.getBlockNumber().longValue() - last_blockNumber.longValue(); i++) {
                long current = last_blockNumber.longValue() + i + 1;
                log.info("差异区块号 {}", current);
                numbers.add(current);
            }

            List<String> addresss = List.of(Web3Client.contract_address);

            // 处理这个区块列表 numbers
            new TaskHandleBlockNumber(numbers, addresss).run();

            new TaskOpenDraw(numbers, addresss).run();

        } catch (Exception e) {
            e.printStackTrace();
        }

        is_run = false;

    }
}
