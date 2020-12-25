package com.atguigu.springcloud.alibaba;

import feign.Feign;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

/**
 * @auther zzyy
 * @create 2020-02-24 16:26
 */
@SpringBootApplication(exclude = FeignAutoConfiguration.class)
@EnableDiscoveryClient
@EnableDubbo
public class MainApp8401 {
    public static void main(String[] args) {
        SpringApplication.run(MainApp8401.class, args);
    }
}
