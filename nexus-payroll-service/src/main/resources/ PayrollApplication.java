package com.zidio.nexushr.payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.zidio.nexushr.common.model")
@EnableJpaRepositories(basePackages = "com.zidio.nexushr.payroll.repository")
public class PayrollApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayrollApplication.class, args);
        System.out.println("🚀 NexusHR Payroll Service Started Successfully!");
    }
}