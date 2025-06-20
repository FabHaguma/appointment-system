package com.appointmentsystem.model;

import lombok.Data;

@Data
public class Doctor {
    private Long id;
    private String regNumber;
    private String firstName;
    private String lastName;
    private String name;
    private String gender;
    private boolean isActive = true;
    private Long userId;
    private String specialization;
    private int consultationDuration = 30;
    private String employmentType;
}