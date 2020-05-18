package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.service.RedeemCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class RedeemCodeServiceImplTest {

    @Resource
    private RedeemCodeService redeemCodeService;

    @Test
    public void redeem() {
    }

    @Test
    public void createCodes() {
        redeemCodeService.createCodes(100);
    }
}