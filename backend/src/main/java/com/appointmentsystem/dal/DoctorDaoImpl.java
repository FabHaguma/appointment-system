package com.appointmentsystem.dal;

import com.appointmentsystem.dal.mappers.DoctorRowMapper;
import com.appointmentsystem.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DoctorDaoImpl implements DoctorDao {

    private final JdbcTemplate jdbcTemplate;
    private final DoctorRowMapper doctorRowMapper = new DoctorRowMapper();

    @Autowired
    public DoctorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Doctor createDoctor(Long userId, String regNumber, String firstName, String lastName, String name, String gender, String specialization, int consultationDuration, String employmentType) {
        String sql = "SELECT * FROM sp_create_doctor(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.queryForObject(sql, doctorRowMapper, userId, regNumber, firstName, lastName, name, gender, specialization, consultationDuration, employmentType);
    }

    @Override
    public List<Doctor> findAll() {
        String sql = "SELECT * FROM sp_get_all_doctors()";
        return jdbcTemplate.query(sql, doctorRowMapper);
    }

    @Override
    public List<Doctor> findByIsActiveTrue() {
        String sql = "SELECT * FROM sp_get_active_doctors()";
        return jdbcTemplate.query(sql, doctorRowMapper);
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        String sql = "SELECT * FROM sp_find_doctor_by_id(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, doctorRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Doctor> findByUserUsername(String username) {
        String sql = "SELECT * FROM sp_find_doctor_by_user_username(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, doctorRowMapper, username));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Doctor> updateDoctor(Long id, Doctor doctorDetails) {
        String sql = "SELECT * FROM sp_update_doctor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, doctorRowMapper,
                    id,
                    doctorDetails.getFirstName(),
                    doctorDetails.getLastName(),
                    "Dr. " + doctorDetails.getFirstName() + " " + doctorDetails.getLastName(),
                    doctorDetails.getGender(),
                    doctorDetails.getRegNumber(),
                    doctorDetails.isActive(),
                    doctorDetails.getSpecialization(),
                    doctorDetails.getConsultationDuration(),
                    doctorDetails.getEmploymentType()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setActiveStatus(Long id, boolean isActive) {
        // Use `update` for stored procedures that return VOID
        String sql = "CALL sp_set_doctor_active_status(?, ?)";
        jdbcTemplate.update(sql, id, isActive);
    }
}