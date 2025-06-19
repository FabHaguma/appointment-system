package com.appointmentsystem.repository;

import com.appointmentsystem.model.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
}
