package com.appointmentsystem.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String actualName;
    private String password;
    private String role;
}