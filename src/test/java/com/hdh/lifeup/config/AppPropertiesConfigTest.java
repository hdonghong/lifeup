package com.hdh.lifeup.config;

import com.hdh.lifeup.util.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppPropertiesConfigTest {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private YbConfig ybConfig;

    @Test
    public void test() {
        System.out.println(appConfig);
        System.out.println(ybConfig);

        String redirectUri = "redirect";
        String callback = appConfig.getPath()
                + Optional.ofNullable(redirectUri).orElse(ybConfig.getRedirectUri());
        String oauthPath = String.format(ybConfig.getOauthPath(), ybConfig.getAppId(), callback);
        System.out.println(Result.success(oauthPath));
    }
}