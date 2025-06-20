package com.appointmentsystem.dal;

import com.appointmentsystem.dal.mappers.NurseRowMapper;
import com.appointmentsystem.model.Nurse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NurseDaoImpl implements NurseDao {

    private final JdbcTemplate jdbcTemplate;
    private final NurseRowMapper nurseRowMapper = new NurseRowMapper();

    @Autowired
    public NurseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Nurse createNurse(Long userId, String regNumber, String firstName, String lastName, String name, String gender) {
        String sql = "SELECT * FROM sp_create_nurse(?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.queryForObject(sql, nurseRowMapper, userId, regNumber, firstName, lastName, name, gender);
    }

    @Override
    public List<Nurse> findAll() {
        String sql = "SELECT * FROM sp_get_all_nurses()";
        return jdbcTemplate.query(sql, nurseRowMapper);
    }

    @Override
    public Optional<Nurse> findById(Long id) {
        String sql = "SELECT * FROM sp_find_nurse_by_id(?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, nurseRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Nurse> updateNurse(Long id, Nurse nurseDetails) {
        String sql = "SELECT * FROM sp_update_nurse(?, ?, ?, ?, ?, ?, ?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, nurseRowMapper,
                    id,
                    nurseDetails.getFirstName(),
                    nurseDetails.getLastName(),
                    nurseDetails.getFirstName() + " " + nurseDetails.getLastName(),
                    nurseDetails.getGender(),
                    nurseDetails.getRegNumber(),
                    nurseDetails.isActive()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setActiveStatus(Long id, boolean isActive) {
        String sql = "CALL sp_set_nurse_active_status(?, ?)";
        jdbcTemplate.update(sql, id, isActive);
    }
}