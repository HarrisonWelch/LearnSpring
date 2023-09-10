package com.harrison.client.service;

import com.harrison.client.entity.PasswordResetToken;
import com.harrison.client.entity.User;
import com.harrison.client.entity.VerificationToken;
import com.harrison.client.model.UserModel;
import com.harrison.client.repository.PasswordResetTokenRepository;
import com.harrison.client.repository.UserRepository;
import com.harrison.client.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken
                = new VerificationToken(user, token);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerficationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        // Does that verification exist at all?
        if (verificationToken == null) {
            return "invalid";
        }

        // Pull user
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        // Check expiration
        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        // User allowed in system
        user.setEnabled(true);
        userRepository.save(user);

        // Pass back approve
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerification(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        // Does that verification exist at all?
        if (passwordResetToken == null) {
            return "invalid";
        }

        // Pull user
        Calendar calendar = Calendar.getInstance();

        // Check expiration
        if ((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        // Pass back approve
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
