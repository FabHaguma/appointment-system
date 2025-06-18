package com.appointmentsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private Long doctorId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String reasonForVisit;
    private LocalDateTime startTime;
}