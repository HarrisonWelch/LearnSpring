package com.harrison.client.service;

import com.harrison.client.entity.User;
import com.harrison.client.model.UserModel;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(User user, String token);
}
