package com.appointmentsystem.dto;

import lombok.Data;

@Data
public class CreateDoctorRequest {
    private String name;
    private String specialization;
    private String employmentType;
    private int consultationDuration;
    private String username;
    private String password;
}
