package com.dam.restaurante.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dam.restaurante.dto.RestauranteDTO;
import com.dam.restaurante.model.Restaurante;
import com.dam.restaurante.repository.RestauranteRepository;

@Service
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Restaurante crearRestaurante(Restaurante restaurante) {
        return restauranteRepository.save(restaurante);
    }

    public List<RestauranteDTO> obtenerTodos() {
        return restauranteRepository.findAll()
                .stream()
                .map(RestauranteDTO::new)
                .collect(Collectors.toList());
    }

    public Restaurante obtenerPorNombre(String nombre) {
        return restauranteRepository.findByNombre(nombre);
    }
    public Optional<Restaurante> obtenerPorId(Long id) {
        return restauranteRepository.findById(id);
    }
}
