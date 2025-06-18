package com.appointmentsystem.dto;

import lombok.Data;

@Data
public class UpdateCredentialsRequest {
    private String newUsername;
    private String newPassword;
}
