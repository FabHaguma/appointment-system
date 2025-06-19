package com.appointmentsystem.dto;

import lombok.Data;

@Data
public class CreateNurseRequest {
    private String regNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private String password;
}
