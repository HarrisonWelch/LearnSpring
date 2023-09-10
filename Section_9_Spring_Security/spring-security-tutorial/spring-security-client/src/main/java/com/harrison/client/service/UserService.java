package com.harrison.client.service;

import com.harrison.client.entity.User;
import com.harrison.client.entity.VerificationToken;
import com.harrison.client.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);

    String validateVerficationToken(String token);

    VerificationToken generateNewVerification(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);
}
