-- This file will be executed automatically if ddl-auto is not 'create'
-- It populates the doctors table for initial testing.
-- Note: Use single quotes for string values in SQL.
INSERT INTO doctors (id, name, is_active) VALUES (1, 'Dr. Alice Carter', true) ON CONFLICT (id) DO NOTHING;
INSERT INTO doctors (id, name, is_active) VALUES (2, 'Dr. Ben Daniels', true) ON CONFLICT (id) DO NOTHING;

-- The password 'password' is hashed using BCrypt
-- You can generate your own here: https://www.bcryptcalculator.com/
INSERT INTO users (id, username, password, role) VALUES (1, 'admin', '$2a$10$4k00QMhXsCS.yusyrYvhmOFJaKuCpFTutkhBTLnuFyPK9KDa09d7i', 'ADMIN') ON CONFLICT (id) DO NOTHING;
INSERT INTO users (id, username, password, role) VALUES (2, 'nurse', '$2a$10$4k00QMhXsCS.yusyrYvhmOFJaKuCpFTutkhBTLnuFyPK9KDa09d7i', 'NURSE') ON CONFLICT (id) DO NOTHING;
INSERT INTO users (id, username, password, role) VALUES (3, 'alice', '$2a$10$4k00QMhXsCS.yusyrYvhmOFJaKuCpFTutkhBTLnuFyPK9KDa09d7i', 'DOCTOR') ON CONFLICT (id) DO NOTHING;

-- Link Dr. Alice Carter (id=1) to the 'alice' user account (id=3)
UPDATE doctors SET user_id = 3 WHERE id = 1;