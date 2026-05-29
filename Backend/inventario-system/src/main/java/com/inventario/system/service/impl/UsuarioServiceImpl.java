package com.inventario.system.service.impl;

import com.inventario.system.dto.UsuarioDTO;
import com.inventario.system.dto.UsuarioSaveDTO;
import com.inventario.system.entity.Usuario;
import com.inventario.system.mapper.UsuarioMapper;
import com.inventario.system.repository.UsuarioRepository;
import com.inventario.system.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id)); // Esto luego sería atrapado por el GlobalExceptionHandler
    }

    @Override
    public UsuarioDTO save(UsuarioSaveDTO dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria para nuevos usuarios");
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .username(dto.getUsername())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .rol(dto.getRol())
                .estado(dto.getEstado() != null ? dto.getEstado() : true)
                .build();

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public UsuarioDTO update(Long id, UsuarioSaveDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setNombre(dto.getNombre());
        usuario.setRol(dto.getRol());
        usuario.setEstado(dto.getEstado());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuarioRepository.delete(usuario);
    }
}
