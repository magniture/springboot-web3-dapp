package com.dapp.springbootweb3dapp.utils.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 确保应用退出时能关闭后台线程
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class ShutdownManager implements DisposableBean {


    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    /**
     * 停止异步执行任务
     */
    private void shutdownAsyncManager() {
        try {
            ThreadPoolExecutor executor = ThreadPoolConfig.getExecutor();
            if (executor == null) {
                return;
            }
            log.info("====关闭后台任务任务线程池====");
            Threads.shutdownAndAwaitTermination(executor);
            log.info("====后台任务关闭成功====");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
