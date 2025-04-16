package com.dapp.springbootweb3dapp.utils.thread;

import com.sunyoki.seeker.config.DevConfig;
import com.sunyoki.seeker.utils.spring.SpringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Getter
    public static ThreadPoolExecutor executor;

    public static ThreadPoolExecutor getTransactionNewPool() {
        ThreadPoolExecutor threadExecutorTransactionNewPool = SpringUtils.getBean("threadExecutorTransactionNewPool");
        executor = threadExecutorTransactionNewPool;
        return threadExecutorTransactionNewPool;
    }

    @Bean(name = "threadExecutorTransactionNewPool")
    public ThreadPoolExecutor createThreadExecutorTransactionNewPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(DevConfig.AVAILABLE_PROCESSOR);
        executor.setThreadNamePrefix("div-");

//        executor.setQueueCapacity(queueCapacity);
        // 线程池对拒绝任务(无线程可用)的处理策略 用调用者所在的线程来执行任务；
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.setRejectedExecutionHandler(CustomizeRejectedExecutionHandler.waitTry());

        //设置线程装饰器，解决父子线程之间上下文无法传递问题
        executor.setTaskDecorator(new ContextTaskDecoratorTransactionNew());

        //关键点: 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //关键点：设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(3600);

        executor.initialize();

        return executor.getThreadPoolExecutor();
    }

    public static class ContextTaskDecoratorTransactionNew implements TaskDecorator {


        @NotNull
        @Override
        public Runnable decorate(@NotNull Runnable runnable) {
            //先拿到主线程的上下文对象
            return () -> {
                DataSourceTransactionManager manager = SpringUtils.getBean(DataSourceTransactionManager.class);
                // 手动开启事务  start
                DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
                definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                TransactionStatus transaction = manager.getTransaction(definition);
                try {
                    log.info("当前方法事物状态：{}", TransactionSynchronizationManager.isActualTransactionActive());
                    runnable.run();
                    // 手动提交事务  end
                    manager.commit(transaction);
                } catch (Exception e) {
                    // 回归事物
                    manager.rollback(transaction);
                    log.error("====================捕获线程异常=================");
                    log.error("错误信息:{}", e.getMessage());
                    e.printStackTrace();
                    log.error("===============================================");
                }
            };
        }
    }
}