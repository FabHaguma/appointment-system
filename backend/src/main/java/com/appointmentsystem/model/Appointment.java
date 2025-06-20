package com.appointmentsystem.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Appointment {
    private Long id;
    private Long doctorId;
    private Doctor doctor; 
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String reasonForVisit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}