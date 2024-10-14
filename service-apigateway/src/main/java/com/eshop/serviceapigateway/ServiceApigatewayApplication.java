package com.eshop.serviceapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ServiceApigatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApigatewayApplication.class, args);
    }

}
