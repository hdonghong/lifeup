package com.hdh.lifeup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author  hdonghong
 * @since 2018/07/15
 */
@EnableCaching
@SpringBootApplication
public class LifeupApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeupApplication.class, args);
	}
}
