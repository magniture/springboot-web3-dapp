package com.dapp.springbootweb3dapp;

import com.dapp.springbootweb3dapp.config.DevConfig;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableCaching //开启缓存
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.dapp"})
@MapperScan
@EnableAsync
public class SpringbootWeb3DappApplication {
    //线上端口
    @Value("${server.port}")
    private int pro_port;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWeb3DappApplication.class, args);
    }

    @Bean(value = "XxlJobSpringExecutor")
    public XxlJobSpringExecutor xxlJobExecutor() {
        if (DevConfig.is_dev) {
            return null;
        }
//        if (1 + 1 == 2) {
//            return null;
//        }
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();

        // 旧机器
        xxlJobSpringExecutor.setAdminAddresses("http://127.0.0.1:8080/xxl-job-admin");

        xxlJobSpringExecutor.setAppname("seeker-app");
        try {
            xxlJobSpringExecutor.setIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ignored) {

        }

        xxlJobSpringExecutor.setPort(pro_port * 2);
        xxlJobSpringExecutor.setAccessToken("23232323231313131313131");
        xxlJobSpringExecutor.setLogPath("./logs");
        xxlJobSpringExecutor.setLogRetentionDays(10);
        return xxlJobSpringExecutor;
    }

}
