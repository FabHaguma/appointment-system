package com.appointmentsystem.service;

import com.appointmentsystem.dal.DoctorDao;
import com.appointmentsystem.dal.NurseDao;
import com.appointmentsystem.dto.CreateDoctorRequest;
import com.appointmentsystem.dto.CreateNurseRequest;
import com.appointmentsystem.model.Doctor;
import com.appointmentsystem.model.Nurse;
import com.appointmentsystem.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UserService userService;
    private final DoctorDao doctorDao;
    private final NurseDao nurseDao;

    public AdminService(UserService userService, DoctorDao doctorDao, NurseDao nurseDao) {
        this.userService = userService;
        this.doctorDao = doctorDao;
        this.nurseDao = nurseDao;
    }

    // --- Doctor Management ---

    @Transactional
    public Doctor createDoctor(CreateDoctorRequest request) {
        String actualName = "Dr. " + request.getFirstName() + " " + request.getLastName();
        
        // 1. Create the User account. Throws exception if username exists.
        User newUser = userService.createUser(
            request.getRegNumber(),
            actualName,
            request.getPassword(),
            "DOCTOR"
        );

        // 2. Create the Doctor profile, linking it to the new User.
        return doctorDao.createDoctor(
                newUser.getId(),
                request.getRegNumber(),
                request.getFirstName(),
                request.getLastName(),
                actualName,
                request.getGender(),
                request.getSpecialization(),
                request.getConsultationDuration(),
                request.getEmploymentType()
        );
    }
    
    public List<Doctor> getAllDoctors() {
        return doctorDao.findAll();
    }

    @Transactional
    public Doctor updateDoctor(Long doctorId, Doctor doctorDetails) {
        doctorDao.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));
        
        return doctorDao.updateDoctor(doctorId, doctorDetails)
                .orElseThrow(() -> new RuntimeException("Failed to update doctor with ID: " + doctorId));
    }

    @Transactional
    public void setDoctorActiveStatus(Long doctorId, boolean isActive) {
        doctorDao.findById(doctorId)
                 .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));
        doctorDao.setActiveStatus(doctorId, isActive);
    }


    // --- Nurse Management ---

    @Transactional
    public Nurse createNurse(CreateNurseRequest request) {
        String actualName = request.getFirstName() + " " + request.getLastName();

        User newUser = userService.createUser(
            request.getRegNumber(),
            actualName,
            request.getPassword(),
            "NURSE"
        );

        return nurseDao.createNurse(
                newUser.getId(),
                request.getRegNumber(),
                request.getFirstName(),
                request.getLastName(),
                actualName,
                request.getGender()
        );
    }
    
    public List<Nurse> getAllNurses() {
        return nurseDao.findAll();
    }

    @Transactional
    public Nurse updateNurse(Long nurseId, Nurse nurseDetails) {
        nurseDao.findById(nurseId)
                .orElseThrow(() -> new IllegalArgumentException("Nurse not found with ID: " + nurseId));
        
        return nurseDao.updateNurse(nurseId, nurseDetails)
                .orElseThrow(() -> new RuntimeException("Failed to update nurse with ID: " + nurseId));
    }

    @Transactional
    public void setNurseActiveStatus(Long nurseId, boolean isActive) {
        nurseDao.findById(nurseId)
                .orElseThrow(() -> new IllegalArgumentException("Nurse not found with ID: " + nurseId));
        nurseDao.setActiveStatus(nurseId, isActive);
    }
}