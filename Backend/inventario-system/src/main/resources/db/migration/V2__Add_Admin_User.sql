-- V2__Add_Admin_User.sql
-- Inserta el usuario administrador inicial con contraseña 'admin123' encriptada con BCrypt
-- NOTA: Este hash corresponde a la contraseña 'admin123'
-- Para producción, cambia esta contraseña inmediatamente después del primer login.

INSERT INTO usuarios (nombre, username, password_hash, rol, estado)
VALUES (
    'Administrador',
    'admin',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoOa.GqhLiXGqiZOOc5bfnDuevmBDPkS0e',
    'ADMIN',
    true
);

-- También insertamos un usuario almacenero de ejemplo
INSERT INTO usuarios (nombre, username, password_hash, rol, estado)
VALUES (
    'Juan Almacenero',
    'almacenero',
    '$2a$10$slYQmyNdgTY18LMsssFBNOW0e.fmHZaQBf11vbxEEn7eTCfmzQ/.W',
    'ALMACENERO',
    true
);

-- Contraseñas:
-- admin      -> admin123
-- almacenero -> almacen123
