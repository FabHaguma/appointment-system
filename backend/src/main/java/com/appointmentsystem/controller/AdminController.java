package com.appointmentsystem.controller;

import com.appointmentsystem.dto.CreateDoctorRequest;
import com.appointmentsystem.dto.CreateNurseRequest;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.model.Nurse;
import com.appointmentsystem.model.User;
import com.appointmentsystem.repository.DoctorRepository;
import com.appointmentsystem.repository.NurseRepository;
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
    private final NurseRepository nurseRepository;
    private final UserService userService;

    public AdminController(DoctorRepository doctorRepository, UserService userService, NurseRepository nurseRepository) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.nurseRepository = nurseRepository;
    }

    // A new endpoint for admins to get ALL doctors, including inactive ones
    @GetMapping("/doctors")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody CreateDoctorRequest request) {
        String actualName = "Dr. " + request.getFirstName() + " " + request.getLastName();
        User newUser = userService.createUser(request.getRegNumber(), actualName, request.getPassword(), "DOCTOR");

        Doctor newDoctor = new Doctor();
        newDoctor.setRegNumber(request.getRegNumber());
        newDoctor.setFirstName(request.getFirstName());
        newDoctor.setLastName(request.getLastName());
        newDoctor.setGender(request.getGender());
        newDoctor.setName(actualName);
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
                    doctor.setFirstName(doctorDetails.getFirstName());
                    doctor.setLastName(doctorDetails.getLastName());
                    doctor.setName("Dr. " + doctorDetails.getFirstName() + " " + doctorDetails.getLastName());
                    doctor.setGender(doctorDetails.getGender());
                    doctor.setRegNumber(doctorDetails.getRegNumber());
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

    // Nurse endpoints
    @GetMapping("/nurses")
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    @PostMapping("/nurses")
    public ResponseEntity<Nurse> createNurse(@RequestBody CreateNurseRequest request) {
        String actualName = request.getFirstName() + " " + request.getLastName();
        User newUser = userService.createUser(request.getRegNumber(), actualName, request.getPassword(), "NURSE");

        Nurse newNurse = new Nurse();
        newNurse.setRegNumber(request.getRegNumber());
        newNurse.setFirstName(request.getFirstName());
        newNurse.setLastName(request.getLastName());
        newNurse.setGender(request.getGender());
        newNurse.setName(actualName);
        newNurse.setUser(newUser);
        newNurse.setActive(true);

        Nurse savedNurse = nurseRepository.save(newNurse);
        return ResponseEntity.ok(savedNurse);
    }

    @PutMapping("/nurses/{nurseId}")
    public ResponseEntity<Nurse> updateNurse(@PathVariable Long nurseId, @RequestBody Nurse nurseDetails) {
        return nurseRepository.findById(nurseId)
                .map(nurse -> {
                    nurse.setFirstName(nurseDetails.getFirstName());
                    nurse.setLastName(nurseDetails.getLastName());
                    nurse.setName(nurseDetails.getFirstName() + " " + nurseDetails.getLastName());
                    nurse.setGender(nurseDetails.getGender());
                    nurse.setRegNumber(nurseDetails.getRegNumber());
                    nurse.setActive(nurseDetails.isActive());
                    Nurse updatedNurse = nurseRepository.save(nurse);
                    return ResponseEntity.ok(updatedNurse);
                }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/nurses/{nurseId}/deactivate")
    public ResponseEntity<Nurse> deactivateNurse(@PathVariable Long nurseId) {
        return nurseRepository.findById(nurseId).map(nurse -> {
            nurse.setActive(false);
            nurseRepository.save(nurse);
            return ResponseEntity.ok(nurse);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/nurses/{nurseId}/activate")
    public ResponseEntity<Nurse> activateNurse(@PathVariable Long nurseId) {
        return nurseRepository.findById(nurseId).map(nurse -> {
            nurse.setActive(true);
            nurseRepository.save(nurse);
            return ResponseEntity.ok(nurse);
        }).orElse(ResponseEntity.notFound().build());
    }
}