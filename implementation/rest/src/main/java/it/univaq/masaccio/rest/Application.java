package it.univaq.masaccio.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    //starts the spring server
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
