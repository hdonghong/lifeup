package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.model.dto.AttributeDTO;
import com.hdh.lifeup.service.AttributeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttributeServiceImplTest {

    @Autowired
    private AttributeService attributeService;

    @Test
    public void getByUserId() throws Exception {
        AttributeDTO attributeDTO = attributeService.getByUserId(1033280752341962754L);
        System.out.println(attributeDTO);
    }

}