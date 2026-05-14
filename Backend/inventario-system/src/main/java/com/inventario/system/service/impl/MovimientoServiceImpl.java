package com.inventario.system.service.impl;

import com.inventario.system.dto.MovimientoRequestDTO;
import com.inventario.system.dto.MovimientoResponseDTO;
import com.inventario.system.entity.Movimiento;
import com.inventario.system.entity.Producto;
import com.inventario.system.entity.Usuario;
import com.inventario.system.exception.ResourceNotFoundException;
import com.inventario.system.mapper.MovimientoMapper;
import com.inventario.system.repository.MovimientoRepository;
import com.inventario.system.repository.ProductoRepository;
import com.inventario.system.repository.UsuarioRepository;
import com.inventario.system.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoMapper movimientoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> findAll() {
        return movimientoRepository.findAll().stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoResponseDTO findById(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con id: " + id));
        return movimientoMapper.toDto(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> findByProductoId(Long productoId) {
        return movimientoRepository.findByProductoId(productoId).stream()
                .map(movimientoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MovimientoResponseDTO registrarMovimiento(MovimientoRequestDTO movimientoDto, String username) {
        Producto producto = productoRepository.findById(movimientoDto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + movimientoDto.getProductoId()));

        // El usuario se obtiene del token JWT (pasado como username) — no del cuerpo de la petición
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado: " + username));

        String tipo = movimientoDto.getTipo().toUpperCase();
        if (!tipo.equals("ENTRADA") && !tipo.equals("SALIDA")) {
            throw new IllegalArgumentException("El tipo de movimiento debe ser ENTRADA o SALIDA");
        }

        // Lógica de validación y actualización de stock
        if (tipo.equals("SALIDA")) {
            if (producto.getStockActual() < movimientoDto.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para realizar la salida. Stock actual: " + producto.getStockActual());
            }
            producto.setStockActual(producto.getStockActual() - movimientoDto.getCantidad());
        } else {
            producto.setStockActual(producto.getStockActual() + movimientoDto.getCantidad());
        }
        
        productoRepository.save(producto); // Actualizamos el stock del producto

        Movimiento movimiento = movimientoMapper.toEntity(movimientoDto);
        movimiento.setTipo(tipo);
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);

        Movimiento savedMovimiento = movimientoRepository.save(movimiento);
        return movimientoMapper.toDto(savedMovimiento);
    }
}
