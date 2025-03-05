package com.korean.moduda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ModudaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModudaApplication.class, args);
    }

}
