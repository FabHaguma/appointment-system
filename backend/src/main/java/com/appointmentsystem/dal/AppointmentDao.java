package com.appointmentsystem.dal;

import com.appointmentsystem.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {
    Appointment create(Appointment appointment);
    void cancel(Long appointmentId);
    Optional<Appointment> findById(Long appointmentId);
    boolean existsByDoctorIdAndStartTime(Long doctorId, LocalDateTime startTime);
    List<Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);
}