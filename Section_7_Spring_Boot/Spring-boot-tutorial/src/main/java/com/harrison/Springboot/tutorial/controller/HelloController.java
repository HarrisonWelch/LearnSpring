package com.harrison.Springboot.tutorial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // Component of a particular type, component is added by this annotation
public class HelloController {

    @GetMapping("/") // Very simple now
    public String helloWorld() {
        return "Welcome too Daily Code Buffer!!";
    }
}
