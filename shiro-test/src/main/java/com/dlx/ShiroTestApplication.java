package com.dlx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: donglixiang
 * @date: 2020/5/1 12:36
 * @description:
 */
@SpringBootApplication
@Slf4j
public class ShiroTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiroTestApplication.class, args);
        log.info("Spring Boot ShiroTestApplication start success");
    }
}
