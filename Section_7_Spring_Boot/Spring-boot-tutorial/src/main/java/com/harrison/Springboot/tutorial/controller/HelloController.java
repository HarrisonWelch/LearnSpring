package com.harrison.Springboot.tutorial.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController // Component of a particular type, component is added by this annotation
public class HelloController {

    @RequestMapping(value = "/", method = RequestMethod.GET) // Assign default endpoint to / as a GET
    public String helloWorld() {
        return "Welcome too Daily Code Buffer!!";
    }
}
