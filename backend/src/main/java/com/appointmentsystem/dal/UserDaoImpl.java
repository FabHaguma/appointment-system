package com.appointmentsystem.dal;

import com.appointmentsystem.dal.mappers.UserRowMapper;
import com.appointmentsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(String username, String actualName, String password, String role) {
        String sql = "SELECT * FROM sp_create_user(?, ?, ?, ?)";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username, actualName, password, role);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM sp_find_user_by_username(?)";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateUserCredentials(String currentUsername, String newUsername, String newPassword) {
         String sql = "SELECT * FROM sp_update_user_credentials(?, ?, ?)";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), currentUsername, newUsername, newPassword);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}