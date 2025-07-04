package com.appointmentsystem.dto;

import lombok.Data;

@Data
public class CreateDoctorRequest {
    private String regNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private String specialization;
    private String employmentType;
    private int consultationDuration;
    private String password;
}
