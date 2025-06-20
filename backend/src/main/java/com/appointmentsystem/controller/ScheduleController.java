package com.appointmentsystem.controller;

import com.appointmentsystem.dal.DoctorDao; // Import the DAO
import com.appointmentsystem.dto.ScheduleSlot;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final DoctorDao doctorDao; // Use the DAO

    public ScheduleController(ScheduleService scheduleService, DoctorDao doctorDao) { // Inject the DAO
        this.scheduleService = scheduleService;
        this.doctorDao = doctorDao;
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
        // Use the DAO to find the doctor profile
        Doctor doctor = doctorDao.findByUserUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Doctor profile not found for user: " + username));
        
        return scheduleService.getWeeklySchedule(doctor.getId(), date);
    }
}