-- Flyway Migration V3: Seed an initial administrator user
-- This script seeds the database with an initial administrator user.
-- This hash was generated for 'admin123' using a BCrypt generator.
INSERT INTO users (username, actual_name, password, role)
VALUES ('admin', 'System Administrator', '$2a$10$4k00QMhXsCS.yusyrYvhmOFJaKuCpFTutkhBTLnuFyPK9KDa09d7i', 'ADMIN')
ON CONFLICT (username) DO NOTHING; -- Prevents error if script is run on a DB that already has this user
