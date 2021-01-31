package com.mmc.reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients // 扫描 @FeignClient 注解
//@Import(DruidDataSourceConfig.class)
public class RewardApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardApplication.class, args);
    }

}
