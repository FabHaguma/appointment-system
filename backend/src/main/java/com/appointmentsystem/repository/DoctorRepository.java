package com.appointmentsystem.repository;

import com.appointmentsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByIsActiveTrue();
    Optional<Doctor> findByUserUsername(String username);
}