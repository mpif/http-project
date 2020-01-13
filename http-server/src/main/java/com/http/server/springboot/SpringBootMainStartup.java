package com.http.server.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;


@EnableAutoConfiguration
public class SpringBootMainStartup {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMainStartup.class, args);
    }

}
