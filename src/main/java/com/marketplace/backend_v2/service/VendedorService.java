package com.marketplace.backend_v2.service;

import com.marketplace.backend_v2.dto.VendedorDTO;
import com.marketplace.backend_v2.model.Vendedor;
import com.marketplace.backend_v2.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    public List<VendedorDTO> findAll() {
        log.info("Buscando todos los vendedores");
        return vendedorRepository.findAll().stream()
                .map(VendedorDTO::fromEntity)
                .toList();
    }

    public VendedorDTO findById(Long id) {
        log.info("Buscando vendedor con ID: {}", id);
        return vendedorRepository.findById(id)
                .map(VendedorDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + id));
    }

    public VendedorDTO create(VendedorDTO vendedorDTO) {
        log.info("Creando nuevo vendedor: {}", vendedorDTO.email());

        if (vendedorRepository.existsByEmail(vendedorDTO.email())) {
            throw new RuntimeException("Ya existe un vendedor con el email: " + vendedorDTO.email());
        }

        Vendedor vendedor = Vendedor.builder()
                .nombre(vendedorDTO.nombre())
                .email(vendedorDTO.email())
                .telefono(vendedorDTO.telefono())
                .direccion(vendedorDTO.direccion())
                .rucDni(vendedorDTO.rucDni())
                .estado(vendedorDTO.estado())
                .build();

        Vendedor savedVendedor = vendedorRepository.save(vendedor);
        log.info("Vendedor creado exitosamente con ID: {}", savedVendedor.getId());

        return VendedorDTO.fromEntity(savedVendedor);
    }

    public VendedorDTO update(Long id, VendedorDTO vendedorDTO) {
        log.info("Actualizando vendedor con ID: {}", id);

        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + id));

        if (!vendedor.getEmail().equals(vendedorDTO.email()) &&
                vendedorRepository.existsByEmail(vendedorDTO.email())) {
            throw new RuntimeException("Ya existe un vendedor con el email: " + vendedorDTO.email());
        }

        vendedor.setNombre(vendedorDTO.nombre());
        vendedor.setEmail(vendedorDTO.email());
        vendedor.setTelefono(vendedorDTO.telefono());
        vendedor.setDireccion(vendedorDTO.direccion());
        vendedor.setRucDni(vendedorDTO.rucDni());

        if (vendedorDTO.estado() != null) {
            vendedor.setEstado(vendedorDTO.estado());
        }

        Vendedor updatedVendedor = vendedorRepository.save(vendedor);
        log.info("Vendedor actualizado exitosamente con ID: {}", updatedVendedor.getId());

        return VendedorDTO.fromEntity(updatedVendedor);
    }

    public void deleteById(Long id) {
        log.info("Eliminando vendedor con ID: {}", id);

        if (!vendedorRepository.existsById(id)) {
            throw new RuntimeException("Vendedor no encontrado con ID: " + id);
        }

        vendedorRepository.deleteById(id);
        log.info("Vendedor eliminado exitosamente con ID: {}", id);
    }

    public List<VendedorDTO> findByEstadoTrue() {
        log.info("Buscando vendedores activos");
        return vendedorRepository.findByEstadoTrue().stream()
                .map(VendedorDTO::fromEntity)
                .toList();
    }
}