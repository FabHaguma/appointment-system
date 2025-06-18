package com.appointmentsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // "Available", "Booked", "Unavailable"
    
    // Appointment details (if booked)
    private Long appointmentId;
    private String patientName;
}