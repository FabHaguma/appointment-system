package com.appointmentsystem.controller;

import com.appointmentsystem.dto.AppointmentRequest;
import com.appointmentsystem.model.Appointment;
import com.appointmentsystem.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'NURSE')")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
        try {
            Appointment newAppointment = appointmentService.createAppointment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
        } catch (IllegalStateException e) { // e.g., Slot is booked
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (IllegalArgumentException e) { // e.g., Doctor not found
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'NURSE')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}