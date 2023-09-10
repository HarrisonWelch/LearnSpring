package com.harrison.client.controller;

import com.harrison.client.entity.User;
import com.harrison.client.event.RegistrationCompleteEvent;
import com.harrison.client.model.UserModel;
import com.harrison.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(
                new RegistrationCompleteEvent(
                        user,
                        "url"
                )); // Build Url later
        return "Success";
    }
}
