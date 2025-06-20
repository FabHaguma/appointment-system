package com.appointmentsystem.controller;

import com.appointmentsystem.dal.DoctorDao; // Import the DAO
import com.appointmentsystem.model.Doctor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorDao doctorDao; // Use the DAO

    public DoctorController(DoctorDao doctorDao) { // Inject the DAO
        this.doctorDao = doctorDao;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'NURSE')")
    public List<Doctor> getActiveDoctors() {
        return doctorDao.findByIsActiveTrue(); // Call the DAO method
    }
}