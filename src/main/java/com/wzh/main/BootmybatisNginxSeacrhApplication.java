package com.wzh.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.zhaolong.mapper"})
@ComponentScan(basePackages = {"com.wzh.biz","com.wzh.action"})
public class BootmybatisNginxSeacrhApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootmybatisNginxSeacrhApplication.class, args);
    }

}
