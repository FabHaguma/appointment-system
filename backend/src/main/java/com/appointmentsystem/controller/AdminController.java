package com.appointmentsystem.controller;

import com.appointmentsystem.dto.CreateDoctorRequest;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.model.User;
import com.appointmentsystem.repository.DoctorRepository;
import com.appointmentsystem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')") // All methods in this controller require ADMIN role
public class AdminController {

    private final DoctorRepository doctorRepository;
    private final UserService userService;

    public AdminController(DoctorRepository doctorRepository, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
    }

    // A new endpoint for admins to get ALL doctors, including inactive ones
    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody CreateDoctorRequest request) {
        User newUser = userService.createUser(request.getUsername(), request.getPassword(), "DOCTOR");

        Doctor newDoctor = new Doctor();
        newDoctor.setName(request.getName());
        newDoctor.setSpecialization(request.getSpecialization());
        newDoctor.setEmploymentType(request.getEmploymentType());
        newDoctor.setConsultationDuration(request.getConsultationDuration());
        newDoctor.setUser(newUser);
        newDoctor.setActive(true);

        Doctor savedDoctor = doctorRepository.save(newDoctor);
        return ResponseEntity.ok(savedDoctor);
    }

    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId, @RequestBody Doctor doctorDetails) {
        return doctorRepository.findById(doctorId)
                .map(doctor -> {
                    doctor.setName(doctorDetails.getName());
                    doctor.setActive(doctorDetails.isActive());
                    doctor.setSpecialization(doctorDetails.getSpecialization());
                    doctor.setConsultationDuration(doctorDetails.getConsultationDuration());
                    doctor.setEmploymentType(doctorDetails.getEmploymentType());
                    Doctor updatedDoctor = doctorRepository.save(doctor);
                    return ResponseEntity.ok(updatedDoctor);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/doctors/{doctorId}/deactivate")
    public ResponseEntity<Doctor> deactivateDoctor(@PathVariable Long doctorId) {
        return doctorRepository.findById(doctorId).map(doctor -> {
            doctor.setActive(false);
            doctorRepository.save(doctor);
            return ResponseEntity.ok(doctor);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/doctors/{doctorId}/activate")
    public ResponseEntity<Doctor> activateDoctor(@PathVariable Long doctorId) {
        return doctorRepository.findById(doctorId).map(doctor -> {
            doctor.setActive(true);
            doctorRepository.save(doctor);
            return ResponseEntity.ok(doctor);
        }).orElse(ResponseEntity.notFound().build());
    }
}