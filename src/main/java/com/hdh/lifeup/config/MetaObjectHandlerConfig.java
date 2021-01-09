package com.hdh.lifeup.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author hdonghong
 */
@Configuration
public class MetaObjectHandlerConfig extends MetaObjectHandler {

    @Autowired
    private AppConfig appConfig;

    @Override
    public void insertFill(MetaObject metaObject) {
//        if (!appConfig.isProd()) {
//            System.out.println("insert");
//        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        System.out.println("update");
    }
}
