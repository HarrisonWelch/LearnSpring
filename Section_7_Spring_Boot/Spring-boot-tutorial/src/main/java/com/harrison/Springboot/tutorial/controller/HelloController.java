package com.harrison.Springboot.tutorial.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // Component of a particular type, component is added by this annotation
public class HelloController {

    @Value("${welcome.message}") // fetch from application.properties and make this var equal to it
    private String welcomeMessage;

    @GetMapping("/") // Very simple now
    public String helloWorld() {
        return welcomeMessage;
    }
}
