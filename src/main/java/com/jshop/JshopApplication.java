package com.jshop;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JshopApplication {
    public static void main(String[] args) {
        SpringApplication.run(JshopApplication.class, args);
    }

}
