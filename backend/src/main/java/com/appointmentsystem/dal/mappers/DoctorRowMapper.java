package com.appointmentsystem.dal.mappers;

import com.appointmentsystem.model.Doctor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorRowMapper implements RowMapper<Doctor> {
    @Override
    public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getLong("id"));
        doctor.setUserId(rs.getLong("user_id"));
        doctor.setRegNumber(rs.getString("reg_number"));
        doctor.setFirstName(rs.getString("first_name"));
        doctor.setLastName(rs.getString("last_name"));
        doctor.setName(rs.getString("name"));
        doctor.setGender(rs.getString("gender"));
        doctor.setActive(rs.getBoolean("is_active"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setConsultationDuration(rs.getInt("consultation_duration"));
        doctor.setEmploymentType(rs.getString("employment_type"));
        return doctor;
    }
}