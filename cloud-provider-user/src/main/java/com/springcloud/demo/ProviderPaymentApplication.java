package com.springcloud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProviderPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderPaymentApplication.class,args);
    }
}
