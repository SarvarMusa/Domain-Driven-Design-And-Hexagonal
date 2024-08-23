package com.food.order.system.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.food.order.system.order.service.dataaccess")
@EntityScan(basePackages = "com.food.order.system.order.service.dataaccess")
@SpringBootApplication(scanBasePackages = "com.food.order.system")
public class OrderServiceApplication {
    /*
     * The scanBasePackages is important when working with multiple modules.
     * With this property any packages on the other modules will be scan
     * */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
