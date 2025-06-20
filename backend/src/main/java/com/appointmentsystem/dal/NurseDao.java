package com.appointmentsystem.dal;

import com.appointmentsystem.model.Nurse;
import java.util.List;
import java.util.Optional;

public interface NurseDao {
    Nurse createNurse(Long userId, String regNumber, String firstName, String lastName, String name, String gender);
    List<Nurse> findAll();
    Optional<Nurse> findById(Long id);
    Optional<Nurse> updateNurse(Long id, Nurse nurseDetails);
    void setActiveStatus(Long id, boolean isActive);
}