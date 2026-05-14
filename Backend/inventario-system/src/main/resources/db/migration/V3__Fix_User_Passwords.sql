-- V3__Fix_User_Passwords.sql
-- Los hashes de V2 eran incorrectos. Eliminamos los usuarios iniciales
-- para que el DataInitializer de Spring Boot los recree con BCrypt correcto al arrancar.

DELETE FROM usuarios WHERE username IN ('admin', 'almacenero');
