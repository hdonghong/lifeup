package com.hdh.lifeup.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

/**
 * @author hdonghong
 */
@Configuration
public class MetaObjectHandlerConfig extends MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    System.out.println("插入方法实体填充");
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    System.out.println("更新方法实体填充");
  }
}
