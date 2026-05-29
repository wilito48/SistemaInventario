package com.inventario.system.service;

import com.inventario.system.dto.UsuarioDTO;
import com.inventario.system.dto.UsuarioSaveDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    UsuarioDTO findById(Long id);
    UsuarioDTO save(UsuarioSaveDTO dto);
    UsuarioDTO update(Long id, UsuarioSaveDTO dto);
    void delete(Long id);
}
