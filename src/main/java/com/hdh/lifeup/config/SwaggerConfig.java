package com.hdh.lifeup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig class<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    /** swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等 */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 扫描controller包
                .apis(RequestHandlerSelectors.basePackage("com.hdh.lifeup.controller"))
                // 设置路径筛选
                .paths(PathSelectors.any())
                .build();
    }

    /** 构建 api文档的详细信息函数,注意这里的注解引用的是哪个 */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("LifeUP RestFul API DOC")
                // 描述
                .description("「人升」的后端接口文档，目前仍采用userId区别用户，以后会全部去掉")
                // 创建人
                .contact(new Contact("hdonghong", "https://github.com/hdonghong/lifeup",""))
                // 版本号
                .version("1.0")
                .build();
    }
}
