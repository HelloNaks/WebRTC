package com.tmax.rg.abook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class AbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbookApplication.class, args);
    }

}
