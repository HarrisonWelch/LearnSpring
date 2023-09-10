package com.harrison.client.controller;

import com.harrison.client.entity.User;
import com.harrison.client.entity.VerificationToken;
import com.harrison.client.event.RegistrationCompleteEvent;
import com.harrison.client.model.PasswordModel;
import com.harrison.client.model.UserModel;
import com.harrison.client.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
            user,
            applicationUrl(request)
        )); // Build Url later
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerficationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User Verifies Sucessfully";
        }
        return "Bad User";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        VerificationToken verificationToken = userService.generateNewVerification(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
        return "Verification link sent.";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetTokenMail(user, applicationUrl(request), token);
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid Token";
        }

        // Token valid, check user exists
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {

            // Update the password
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password Reset Successful";
        }
        return "Invalid Token";
    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        // Send Mail to user
        String url = applicationUrl + "/savePassword?token=" + token; // Context path

        // sendVerificationEmail() // Mocking it
        log.info("Click the link to reset your password: {}", url);
        return url;
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
        // Send Mail to user
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken(); // Context path

        // sendVerificationEmail() // Mocking it
        log.info("Click the link to verify your account: {}", url);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
