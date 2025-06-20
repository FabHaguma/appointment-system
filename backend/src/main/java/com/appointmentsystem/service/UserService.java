package com.appointmentsystem.service;

import com.appointmentsystem.dal.UserDao;
import com.appointmentsystem.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(String username, String actualName, String password, String role) {
        if (userDao.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists: " + username);
        }
        String encodedPassword = passwordEncoder.encode(password);
        return userDao.createUser(username, actualName, encodedPassword, role);
    }

    @Transactional
    public User updateUserCredentials(String currentUsername, String newUsername, String newPassword) {
        userDao.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + currentUsername));

        if (newUsername != null && !newUsername.isEmpty() && !newUsername.equals(currentUsername)) {
            if (userDao.findByUsername(newUsername).isPresent()) {
                throw new IllegalStateException("New username is already taken: " + newUsername);
            }
        }

        String encodedPassword = (newPassword != null && !newPassword.isEmpty())
                ? passwordEncoder.encode(newPassword) : null;
        
        return userDao.updateUserCredentials(currentUsername, newUsername, encodedPassword)
                .orElseThrow(() -> new RuntimeException("Failed to update credentials for user: " + currentUsername));
    }
}