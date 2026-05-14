package com.inventario.system.config;

import com.inventario.system.entity.Usuario;
import com.inventario.system.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos que se ejecuta UNA SOLA VEZ al arrancar el backend.
 * Crea los usuarios iniciales con contraseñas correctamente encriptadas con BCrypt.
 * Si los usuarios ya existen, no hace nada (idempotente).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        crearUsuarioSiNoExiste("admin", "Administrador", "admin123", "ADMIN");
        crearUsuarioSiNoExiste("almacenero", "Juan Almacenero", "almacen123", "ALMACENERO");
    }

    private void crearUsuarioSiNoExiste(String username, String nombre, String password, String rol) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            log.info("Usuario '{}' ya existe, omitiendo creación.", username);
            return;
        }

        Usuario usuario = Usuario.builder()
                .username(username)
                .nombre(nombre)
                // BCryptPasswordEncoder genera el hash correcto aquí
                .passwordHash(passwordEncoder.encode(password))
                .rol(rol)
                .estado(true)
                .build();

        usuarioRepository.save(usuario);
        log.info("✅ Usuario '{}' creado con rol '{}' exitosamente.", username, rol);
    }
}
