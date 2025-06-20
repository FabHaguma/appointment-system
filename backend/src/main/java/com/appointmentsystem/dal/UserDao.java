package com.appointmentsystem.dal;

import com.appointmentsystem.model.User;
import java.util.Optional;

public interface UserDao {
    User createUser(String username, String actualName, String password, String role);
    Optional<User> findByUsername(String username);
    Optional<User> updateUserCredentials(String currentUsername, String newUsername, String newPassword);
}