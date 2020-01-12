package com.http.server.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class SpringBootMainStartup {

    @RequestMapping("/")
    String home() {
        System.out.println("welcome home!!!");
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMainStartup.class, args);
    }

}
