package com.appointmentsystem.controller;

import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.repository.DoctorRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'NURSE')") // Only Admins and Nurses can see the full doctor list
    public List<Doctor> getActiveDoctors() {
        return doctorRepository.findByIsActiveTrue();
    }
}