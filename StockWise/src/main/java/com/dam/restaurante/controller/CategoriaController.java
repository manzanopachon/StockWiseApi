package com.dam.restaurante.controller;

import com.dam.restaurante.dto.CategoriaDTO;

import com.dam.restaurante.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*") // Permitir llamadas desde frontend
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<CategoriaDTO> obtenerCategorias() {
        return categoriaRepository.findAll().stream()
                .map(cat -> new CategoriaDTO(cat.getId(), cat.getNombre()))
                .collect(Collectors.toList());
    }
}
