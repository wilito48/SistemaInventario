package com.inventario.system.controller;

import com.inventario.system.dto.AuthResponseDTO;
import com.inventario.system.dto.LoginRequestDTO;
import com.inventario.system.entity.Usuario;
import com.inventario.system.repository.UsuarioRepository;
import com.inventario.system.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpiration;

    // NOTA DE SEGURIDAD: Snyk puede alertar sobre CSRF aquí, pero es un falso positivo.
    // Esta API REST usa JWT (stateless), por lo que CSRF está deshabilitado intencionalmente en SecurityConfig.
    // Las APIs stateless con JWT son inmunes a CSRF por diseño (el token no se envía por cookies).
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            // Delegamos la autenticación a Spring Security (verifica usuario + contraseña con BCrypt)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuario o contraseña incorrectos"));
        }

        // Si llegamos aquí, el usuario es válido
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        // Obtenemos los datos del usuario para la respuesta
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername()).orElseThrow();

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .expiresIn(jwtExpiration)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(org.springframework.security.core.Authentication authentication) {
        // Devuelve los datos del usuario autenticado actualmente (útil al recargar la app)
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "username", usuario.getUsername(),
                "nombre", usuario.getNombre(),
                "rol", usuario.getRol()
        ));
    }
}
