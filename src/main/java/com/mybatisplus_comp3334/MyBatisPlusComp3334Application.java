package com.mybatisplus_comp3334;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mybatisplus_comp3334.mapper")
public class MyBatisPlusComp3334Application {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisPlusComp3334Application.class, args);
    }

}
