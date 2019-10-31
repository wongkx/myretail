package com.myretail

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
public class ServerRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerRunner.class).run()
    }

}