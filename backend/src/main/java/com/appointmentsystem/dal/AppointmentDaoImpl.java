package com.appointmentsystem.dal;

import com.appointmentsystem.dal.mappers.AppointmentRowMapper;
import com.appointmentsystem.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    private final JdbcTemplate jdbcTemplate;
    private final AppointmentRowMapper appointmentRowMapper = new AppointmentRowMapper();

    @Autowired
    public AppointmentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Appointment create(Appointment appointment) {
        String sql = "SELECT * FROM sp_create_appointment(?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.queryForObject(sql, appointmentRowMapper,
                appointment.getDoctorId(),
                appointment.getPatientName(),
                appointment.getPatientEmail(),
                appointment.getPatientPhone(),
                appointment.getReasonForVisit(),
                appointment.getStartTime(),
                appointment.getEndTime());
    }

    @Override
    public void cancel(Long appointmentId) {
        String sql = "CALL sp_cancel_appointment(?)";
        jdbcTemplate.update(sql, appointmentId);
    }

    @Override
    public Optional<Appointment> findById(Long appointmentId) {
        String sql = "SELECT * FROM sp_find_appointment_by_id(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, appointmentRowMapper, appointmentId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByDoctorIdAndStartTime(Long doctorId, LocalDateTime startTime) {
        String sql = "SELECT sp_exists_by_doctor_id_and_start_time(?, ?)";
        // Use queryForObject for single value returns like boolean, int, etc.
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, doctorId, startTime);
        return exists != null && exists;
    }

    @Override
    public List<Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM sp_find_appointments_by_doctor_and_time_range(?, ?, ?)";
        return jdbcTemplate.query(sql, appointmentRowMapper, doctorId, start, end);
    }
}