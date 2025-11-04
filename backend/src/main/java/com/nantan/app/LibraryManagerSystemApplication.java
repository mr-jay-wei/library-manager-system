package com.nantan.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
// 关键修改: 精确指定各自 Repository 的扫描路径
@EnableJpaRepositories(basePackages = "com.nantan.app.jpa")
@EnableMongoRepositories(basePackages = "com.nantan.app.mongo")
@EnableAsync
public class LibraryManagerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagerSystemApplication.class, args);
    }
}