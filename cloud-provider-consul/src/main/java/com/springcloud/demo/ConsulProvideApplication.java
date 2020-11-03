package com.springcloud.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsulProvideApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsulProvideApplication.class,args);
    }
}
