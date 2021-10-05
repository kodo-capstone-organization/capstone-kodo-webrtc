package com.spring.kodowebrtc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
public class KodowebrtcApplication {

    public static void main(String[] args) {
        SpringApplication.run(KodowebrtcApplication.class, args);
    }

}
