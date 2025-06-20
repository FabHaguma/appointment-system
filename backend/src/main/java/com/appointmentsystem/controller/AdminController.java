package com.appointmentsystem.controller;

import com.appointmentsystem.dto.CreateDoctorRequest;
import com.appointmentsystem.dto.CreateNurseRequest;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.model.Nurse;
import com.appointmentsystem.service.AdminService; // Import the new service
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    // Inject AdminService instead of individual repositories/services
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // --- Doctor Endpoints ---

    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return adminService.getAllDoctors();
    }

    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody CreateDoctorRequest request) {
        try {
            Doctor savedDoctor = adminService.createDoctor(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
        } catch (IllegalStateException e) {
            // e.g., Username already exists
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId, @RequestBody Doctor doctorDetails) {
        try {
            Doctor updatedDoctor = adminService.updateDoctor(doctorId, doctorDetails);
            return ResponseEntity.ok(updatedDoctor);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/doctors/{doctorId}/deactivate")
    public ResponseEntity<Void> deactivateDoctor(@PathVariable Long doctorId) {
        try {
            adminService.setDoctorActiveStatus(doctorId, false);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/doctors/{doctorId}/activate")
    public ResponseEntity<Void> activateDoctor(@PathVariable Long doctorId) {
         try {
            adminService.setDoctorActiveStatus(doctorId, true);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // --- Nurse Endpoints ---

    @GetMapping("/nurses")
    public List<Nurse> getAllNurses() {
        return adminService.getAllNurses();
    }

    @PostMapping("/nurses")
    public ResponseEntity<Nurse> createNurse(@RequestBody CreateNurseRequest request) {
        try {
            Nurse savedNurse = adminService.createNurse(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNurse);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PutMapping("/nurses/{nurseId}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable Long nurseId, @RequestBody Nurse nurseDetails) {
        try {
            Nurse updatedNurse = adminService.updateNurse(nurseId, nurseDetails);
            return ResponseEntity.ok(updatedNurse);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/nurses/{nurseId}/deactivate")
    public ResponseEntity<Void> deactivateNurse(@PathVariable Long nurseId) {
        try {
            adminService.setNurseActiveStatus(nurseId, false);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/nurses/{nurseId}/activate")
    public ResponseEntity<Void> activateNurse(@PathVariable Long nurseId) {
        try {
            adminService.setNurseActiveStatus(nurseId, true);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}