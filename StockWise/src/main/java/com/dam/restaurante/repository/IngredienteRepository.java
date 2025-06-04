package com.dam.restaurante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dam.restaurante.model.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    // Buscar por nombre
    Ingrediente findByNombre(String nombre);
    //Listar ingredientes de un restaurante
    List<Ingrediente> findByRestauranteId(Long restauranteId);

}

