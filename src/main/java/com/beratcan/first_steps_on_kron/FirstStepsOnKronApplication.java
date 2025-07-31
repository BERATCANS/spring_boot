package com.beratcan.first_steps_on_kron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FirstStepsOnKronApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstStepsOnKronApplication.class, args);
    }

}
