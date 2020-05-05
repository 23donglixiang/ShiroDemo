package com.dlx;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dlx")
@MapperScan("com.dlx.dao")
@Slf4j
public class ShiroApplication {
    public static void main(String[] args){
        SpringApplication.run(ShiroApplication.class, args);
        log.info("Spring Boot ShiroApplication start success！");
    }
}
