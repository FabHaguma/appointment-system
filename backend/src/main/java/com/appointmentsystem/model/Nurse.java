package com.appointmentsystem.model;

import lombok.Data;

@Data
public class Nurse {
    private Long id;
    private Long userId;
    private String regNumber;
    private String firstName;
    private String lastName;
    private String name;
    private String gender;
    private boolean isActive = true;
}