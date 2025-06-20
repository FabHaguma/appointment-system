-- Flyway Migration V1: Initial Tables for Clinic DB

-- users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    actual_name VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- doctors table
CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    reg_number VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    specialization VARCHAR(255) NOT NULL,
    consultation_duration INT DEFAULT 30,
    employment_type VARCHAR(100),
    CONSTRAINT fk_doctor_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE -- If a user is deleted, their doctor profile is also deleted
);

-- nurses table
CREATE TABLE nurses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    reg_number VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_nurse_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE -- If a user is deleted, their nurse profile is also deleted
);

-- appointments table
CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    patient_name VARCHAR(255) NOT NULL,
    patient_email VARCHAR(255),
    patient_phone VARCHAR(50) NOT NULL,
    reason_for_visit TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY(doctor_id)
        REFERENCES doctors(id)
        ON DELETE CASCADE, -- If a doctor is removed, their appointments are also removed
    UNIQUE (doctor_id, start_time) -- Enforces no double booking at the DB level
);

-- indexes for performance on frequently queried columns
CREATE INDEX idx_appointments_doctor_id_start_time ON appointments(doctor_id, start_time);
CREATE INDEX idx_doctors_user_username ON doctors(user_id); -- For finding doctor by user