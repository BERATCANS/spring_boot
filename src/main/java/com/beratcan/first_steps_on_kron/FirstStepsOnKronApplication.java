package com.beratcan.first_steps_on_kron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class FirstStepsOnKronApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstStepsOnKronApplication.class, args);
    }

}
