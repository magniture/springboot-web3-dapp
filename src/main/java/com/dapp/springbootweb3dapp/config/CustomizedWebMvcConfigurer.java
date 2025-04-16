package com.dapp.springbootweb3dapp.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.sunyoki.seeker.config.json.LongToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class CustomizedWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {


        //删除jackson的消息转换器
        converters.removeIf(item -> item instanceof MappingJackson2HttpMessageConverter);


        //定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();


        //添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteBigIntAsStr);  // 关键配置：将大整数以字符串形式输出
        fastJsonConfig.setSerializerFeatures(
                //json格式化
                SerializerFeature.PrettyFormat,
//                SerializerFeature.WriteBigIntAsStr,
                //输出map中value为null的数据
//                SerializerFeature.WriteMapNullValue,
                //输出空list为[]，而不是null
//                SerializerFeature.WriteNullListAsEmpty,
                //输出空string为""，而不是null、
//                SerializerFeature.WriteNonStringValueAsString,
//                SerializerFeature.WriteNonStringKeyAsString,
                //Boolean字段如果为null,输出为false,而非null
//                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.DisableCircularReferenceDetect
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);


        // 注册自定义的Long类型序列化器
        SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();
        serializeConfig.put(Long.class, new LongToStringSerializer());


        //在converter中添加配置信息
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);


//        SerializeConfig serializeConfig = fastJsonConfig.getSerializeConfig();
//        serializeConfig.put(String.class, MeToStringSerializer.instance);
//        fastJsonConfig.setSerializeConfig(serializeConfig);


        List<MediaType> mediaTypes = new ArrayList<>();

        mediaTypes.add(MediaType.APPLICATION_JSON);


        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        //fastjson转换器必须放在StringHttpMessageConverter之后，不然接口返回值如果是字符串，会多加一对双引号
        converters.add(0, fastJsonHttpMessageConverter);
    }

}
