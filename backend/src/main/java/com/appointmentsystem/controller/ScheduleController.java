package com.appointmentsystem.controller;

import com.appointmentsystem.dto.ScheduleSlot;
import com.appointmentsystem.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
// ... imports
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.appointmentsystem.repository.DoctorRepository;
import com.appointmentsystem.model.Doctor;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final DoctorRepository doctorRepository;

    public ScheduleController(ScheduleService scheduleService, DoctorRepository doctorRepository) {
        this.scheduleService = scheduleService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctors/{doctorId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'NURSE')")
    public List<ScheduleSlot> getWeeklyScheduleForAdminOrNurse(
        @PathVariable Long doctorId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return scheduleService.getWeeklySchedule(doctorId, date);
    }
    
    @GetMapping("/my-schedule")
    @PreAuthorize("hasAuthority('DOCTOR')")
    public List<ScheduleSlot> getMyWeeklySchedule(
        Authentication authentication,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String username = authentication.getName();
        Doctor doctor = doctorRepository.findByUserUsername(username) // Create this method in DoctorRepository
            .orElseThrow(() -> new RuntimeException("Doctor profile not found for user: " + username));
        
        return scheduleService.getWeeklySchedule(doctor.getId(), date);
    }
}