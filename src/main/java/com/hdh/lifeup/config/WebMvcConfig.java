package com.hdh.lifeup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfig class<br/>
 *
 * @author hdonghong
 * @since 2018/08/19
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 这里匹配了所有的URL，允许所有的外域发起跨域请求，允许外域发起请求任意HTTP Method，允许跨域请求包含任意的头信息。
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);
    }

/*    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);

        // builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);

//         builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        builder.serializerByType(LocalDateTime.class,new LocalDateTime2LongSerializer());
        // 自定义Long类型转换 超过12个数字用String格式返回，由于js的number只能表示15个数字
//        builder.serializerByType(Long.TYPE,new LocalDateTime2LongSerializer());
        converters.add(0,new MappingJackson2HttpMessageConverter(builder.build()));
    }*/
}
