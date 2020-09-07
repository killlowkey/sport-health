package com.xzc.sport.health;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xzc.sport.health.mapper")
public class SportHealthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportHealthApplication.class, args);
    }

}
