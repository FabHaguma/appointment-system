-- Flyway Migration V2: application stored procedures

-- =================================================================
-- User Stored Procedures
-- =================================================================

-- Create a new user
CREATE OR REPLACE FUNCTION sp_create_user(p_username VARCHAR, p_actual_name VARCHAR, p_password VARCHAR, p_role VARCHAR)
RETURNS users AS $$
DECLARE new_user users;
BEGIN
    INSERT INTO users (username, actual_name, password, role) VALUES (p_username, p_actual_name, p_password, p_role) RETURNING * INTO new_user;
    RETURN new_user;
END;
$$ LANGUAGE plpgsql;

-- Find a user by their username
CREATE OR REPLACE FUNCTION sp_find_user_by_username(p_username VARCHAR)
RETURNS SETOF users AS $$
BEGIN
    RETURN QUERY SELECT * FROM users WHERE username = p_username;
END;
$$ LANGUAGE plpgsql;

-- Update a user's credentials
CREATE OR REPLACE FUNCTION sp_update_user_credentials(p_current_username VARCHAR, p_new_username VARCHAR, p_new_password VARCHAR)
RETURNS users AS $$
DECLARE updated_user users;
BEGIN
    UPDATE users SET username = COALESCE(p_new_username, username), password = COALESCE(p_new_password, password) WHERE username = p_current_username RETURNING * INTO updated_user;
    RETURN updated_user;
END;
$$ LANGUAGE plpgsql;



-- =================================================================
-- Doctor Stored Procedures
-- =================================================================

-- Create a new doctor
CREATE OR REPLACE FUNCTION sp_create_doctor(p_user_id BIGINT, p_reg_number VARCHAR, p_first_name VARCHAR, p_last_name VARCHAR, p_name VARCHAR, p_gender VARCHAR, p_specialization VARCHAR, p_consultation_duration INT, p_employment_type VARCHAR)
RETURNS doctors AS $$
DECLARE new_doctor doctors;
BEGIN
    INSERT INTO doctors (user_id, reg_number, first_name, last_name, name, gender, specialization, consultation_duration, employment_type, is_active) VALUES (p_user_id, p_reg_number, p_first_name, p_last_name, p_name, p_gender, p_specialization, p_consultation_duration, p_employment_type, TRUE) RETURNING * INTO new_doctor;
    RETURN new_doctor;
END;
$$ LANGUAGE plpgsql;

-- Get all doctors
CREATE OR REPLACE FUNCTION sp_get_all_doctors()
RETURNS SETOF doctors AS $$
BEGIN
    RETURN QUERY SELECT * FROM doctors ORDER BY last_name, first_name;
END;
$$ LANGUAGE plpgsql;

-- Get only active doctors
CREATE OR REPLACE FUNCTION sp_get_active_doctors()
RETURNS SETOF doctors AS $$
BEGIN
    RETURN QUERY SELECT * FROM doctors WHERE is_active = TRUE ORDER BY last_name, first_name;
END;
$$ LANGUAGE plpgsql;

-- Find doctor by user's username
-- This requires a JOIN
CREATE OR REPLACE FUNCTION sp_find_doctor_by_user_username(p_username VARCHAR)
RETURNS SETOF doctors AS $$
BEGIN
    RETURN QUERY SELECT d.* FROM doctors d JOIN users u ON d.user_id = u.id WHERE u.username = p_username;
END;
$$ LANGUAGE plpgsql;

-- Find doctor by ID
CREATE OR REPLACE FUNCTION sp_find_doctor_by_id(p_doctor_id BIGINT)
RETURNS SETOF doctors AS $$
BEGIN
    RETURN QUERY SELECT * FROM doctors WHERE id = p_doctor_id;
END;
$$ LANGUAGE plpgsql;

-- Update a doctor's details
CREATE OR REPLACE FUNCTION sp_update_doctor(p_id BIGINT, p_first_name VARCHAR, p_last_name VARCHAR, p_name VARCHAR, p_gender VARCHAR, p_reg_number VARCHAR, p_is_active BOOLEAN, p_specialization VARCHAR, p_consultation_duration INT, p_employment_type VARCHAR)
RETURNS doctors AS $$
DECLARE updated_doctor doctors;
BEGIN
    UPDATE doctors SET first_name = p_first_name, last_name = p_last_name, name = p_name, gender = p_gender, reg_number = p_reg_number, is_active = p_is_active, specialization = p_specialization, consultation_duration = p_consultation_duration, employment_type = p_employment_type WHERE id = p_id RETURNING * INTO updated_doctor;
    RETURN updated_doctor;
END;
$$ LANGUAGE plpgsql;

-- Set doctor's active status
CREATE OR REPLACE FUNCTION sp_set_doctor_active_status(p_id BIGINT, p_is_active BOOLEAN)
RETURNS VOID AS $$
BEGIN
    UPDATE doctors SET is_active = p_is_active WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;


-- =================================================================
-- Nurse Stored Procedures
-- =================================================================

-- Create a new nurse
CREATE OR REPLACE FUNCTION sp_create_nurse(p_user_id BIGINT, p_reg_number VARCHAR, p_first_name VARCHAR, p_last_name VARCHAR, p_name VARCHAR, p_gender VARCHAR)
RETURNS nurses AS $$
DECLARE new_nurse nurses;
BEGIN
    INSERT INTO nurses (user_id, reg_number, first_name, last_name, name, gender, is_active) VALUES (p_user_id, p_reg_number, p_first_name, p_last_name, p_name, p_gender, TRUE) RETURNING * INTO new_nurse;
    RETURN new_nurse;
END;
$$ LANGUAGE plpgsql;

-- Get all nurses
CREATE OR REPLACE FUNCTION sp_get_all_nurses()
RETURNS SETOF nurses AS $$
BEGIN
    RETURN QUERY SELECT * FROM nurses ORDER BY last_name, first_name;
END;
$$ LANGUAGE plpgsql;

-- Set nurse's active status
CREATE OR REPLACE FUNCTION sp_set_nurse_active_status(p_id BIGINT, p_is_active BOOLEAN)
RETURNS VOID AS $$
BEGIN
    UPDATE nurses SET is_active = p_is_active WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

-- Find nurse by ID
CREATE OR REPLACE FUNCTION sp_find_nurse_by_id(p_nurse_id BIGINT)
RETURNS SETOF nurses AS $$
BEGIN
    RETURN QUERY SELECT * FROM nurses WHERE id = p_nurse_id;
END;
$$ LANGUAGE plpgsql;

-- Update a nurse's details
CREATE OR REPLACE FUNCTION sp_update_nurse(p_id BIGINT, p_first_name VARCHAR, p_last_name VARCHAR, p_name VARCHAR, p_gender VARCHAR, p_reg_number VARCHAR, p_is_active BOOLEAN)
RETURNS nurses AS $$
DECLARE updated_nurse nurses;
BEGIN
    UPDATE nurses SET first_name = p_first_name, last_name = p_last_name, name = p_name, gender = p_gender, reg_number = p_reg_number, is_active = p_is_active WHERE id = p_id RETURNING * INTO updated_nurse;
    RETURN updated_nurse;
END;
$$ LANGUAGE plpgsql;


-- =================================================================
-- Appointment Stored Procedures
-- =================================================================

-- Create an appointment
CREATE OR REPLACE FUNCTION sp_create_appointment(p_doctor_id BIGINT, p_patient_name VARCHAR, p_patient_email VARCHAR, p_patient_phone VARCHAR, p_reason_for_visit TEXT, p_start_time TIMESTAMP, p_end_time TIMESTAMP)
RETURNS appointments AS $$
DECLARE new_appointment appointments;
BEGIN
    INSERT INTO appointments (doctor_id, patient_name, patient_email, patient_phone, reason_for_visit, start_time, end_time) VALUES (p_doctor_id, p_patient_name, p_patient_email, p_patient_phone, p_reason_for_visit, p_start_time, p_end_time) RETURNING * INTO new_appointment;
    RETURN new_appointment;
END;
$$ LANGUAGE plpgsql;

-- Delete an appointment
CREATE OR REPLACE FUNCTION sp_cancel_appointment(p_appointment_id BIGINT)
RETURNS VOID AS $$
BEGIN
    DELETE FROM appointments WHERE id = p_appointment_id;
END;
$$ LANGUAGE plpgsql;

-- Find an appointment by ID
CREATE OR REPLACE FUNCTION sp_find_appointment_by_id(p_appointment_id BIGINT)
RETURNS SETOF appointments AS $$
BEGIN
    RETURN QUERY SELECT * FROM appointments WHERE id = p_appointment_id;
END;
$$ LANGUAGE plpgsql;

-- Check if a time slot for a doctor is already booked
CREATE OR REPLACE FUNCTION sp_exists_by_doctor_id_and_start_time(p_doctor_id BIGINT, p_start_time TIMESTAMP)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (SELECT 1 FROM appointments WHERE doctor_id = p_doctor_id AND start_time = p_start_time);
END;
$$ LANGUAGE plpgsql;

-- Get appointments for a doctor within a date range
CREATE OR REPLACE FUNCTION sp_find_appointments_by_doctor_and_time_range(p_doctor_id BIGINT, p_start_range TIMESTAMP, p_end_range TIMESTAMP)
RETURNS SETOF appointments AS $$
BEGIN
    RETURN QUERY SELECT * FROM appointments WHERE doctor_id = p_doctor_id AND start_time BETWEEN p_start_range AND p_end_range;
END;
$$ LANGUAGE plpgsql;