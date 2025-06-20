package com.appointmentsystem.dal.mappers;

import com.appointmentsystem.model.Appointment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentRowMapper implements RowMapper<Appointment> {
    @Override
    public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("id"));
        appointment.setDoctorId(rs.getLong("doctor_id"));
        appointment.setPatientName(rs.getString("patient_name"));
        appointment.setPatientEmail(rs.getString("patient_email"));
        appointment.setPatientPhone(rs.getString("patient_phone"));
        appointment.setReasonForVisit(rs.getString("reason_for_visit"));
        // Convert SQL TIMESTAMP to Java LocalDateTime
        appointment.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        appointment.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
        return appointment;
    }
}