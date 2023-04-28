package com.pan.pandown.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableWebMvc
@MapperScan("com.pan.pandown.dao.mapper")
@ComponentScan("com.pan.pandown")
public class PandownApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandownApplication.class, args);
    }

}
