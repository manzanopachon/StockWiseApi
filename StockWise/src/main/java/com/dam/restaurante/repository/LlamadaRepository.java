package com.dam.restaurante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dam.restaurante.model.LlamadaCamarero;

public interface LlamadaRepository extends JpaRepository<LlamadaCamarero, Long> {
    List<LlamadaCamarero> findByAtendidaFalse();
}