package com.appointmentsystem.dal.mappers;

import com.appointmentsystem.model.Nurse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NurseRowMapper implements RowMapper<Nurse> {
    @Override
    public Nurse mapRow(ResultSet rs, int rowNum) throws SQLException {
        Nurse nurse = new Nurse();
        nurse.setId(rs.getLong("id"));
        nurse.setUserId(rs.getLong("user_id"));
        nurse.setRegNumber(rs.getString("reg_number"));
        nurse.setFirstName(rs.getString("first_name"));
        nurse.setLastName(rs.getString("last_name"));
        nurse.setName(rs.getString("name"));
        nurse.setGender(rs.getString("gender"));
        nurse.setActive(rs.getBoolean("is_active"));
        return nurse;
    }
}