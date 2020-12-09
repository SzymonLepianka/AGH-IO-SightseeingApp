package com.accessingmysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AccessingMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccessingMysqlApplication.class, args);
    }

}