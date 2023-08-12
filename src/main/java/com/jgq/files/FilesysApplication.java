package com.jgq.files;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jgq.files.dao")
public class FilesysApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesysApplication.class, args);
    }

}
