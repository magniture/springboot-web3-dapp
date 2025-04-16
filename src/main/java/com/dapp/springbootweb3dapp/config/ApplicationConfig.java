package com.dapp.springbootweb3dapp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;

// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.sunyoki.**")
public class ApplicationConfig {
    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    /**
     * 文件大小配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        long bytes = 1024 * 1024L * 1024L;
        DataSize dataSize = DataSize.ofBytes(bytes);
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(dataSize);
        factory.setMaxFileSize(dataSize);
        return factory.createMultipartConfig();
    }


    //此方法位于一个有@Configuration注解的类中
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }


}
