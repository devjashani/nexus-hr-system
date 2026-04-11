package com.zidio.nexushr.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.zidio.nexushr"})
@EntityScan(basePackages = "com.zidio.nexushr.common.model")
@EnableJpaRepositories(basePackages = "com.zidio.nexushr.auth.repository")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        System.out.println("🚀 NexusHR Auth Service Started Successfully!");
    }
}