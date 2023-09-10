package com.harrison.client.service;

import com.harrison.client.entity.User;
import com.harrison.client.entity.VerificationToken;
import com.harrison.client.model.UserModel;
import com.harrison.client.repository.UserRepository;
import com.harrison.client.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

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
}
