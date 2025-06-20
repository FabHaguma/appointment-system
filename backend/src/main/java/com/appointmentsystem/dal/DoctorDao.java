package com.appointmentsystem.dal;

import com.appointmentsystem.model.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorDao {
    Doctor createDoctor(Long userId, String regNumber, String firstName, String lastName, String name, String gender, String specialization, int consultationDuration, String employmentType);
    List<Doctor> findAll();
    List<Doctor> findByIsActiveTrue();
    Optional<Doctor> findById(Long id);
    Optional<Doctor> findByUserUsername(String username);
    Optional<Doctor> updateDoctor(Long id, Doctor doctorDetails);
    void setActiveStatus(Long id, boolean isActive);
}