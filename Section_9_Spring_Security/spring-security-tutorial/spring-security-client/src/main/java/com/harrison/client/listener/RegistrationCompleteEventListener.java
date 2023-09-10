package com.harrison.client.listener;

import com.harrison.client.entity.User;
import com.harrison.client.event.RegistrationCompleteEvent;
import com.harrison.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create the verification token for this User
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        // Save this token on the DB (need to make this entity)
        userService.saveVerificationTokenForUser(user, token);
        // Send Mail to user
    }
}
