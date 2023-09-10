package com.harrison.client.listener;

import com.harrison.client.entity.User;
import com.harrison.client.event.RegistrationCompleteEvent;
import com.harrison.client.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.UUID;

@Slf4j
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
        String url = event.getApplicationUrl() + "verifyRegistration?token=" + token; // Context path

        // sendVerificationEmail() // Mocking it
        log.info("Click the link to verify your account: {}", url);
    }
}
