package com.dam.restaurante.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class LlamadaCamarero {
    @Id @GeneratedValue
    private Long id;

    private Long restauranteId;
    private int mesaId;
    private LocalDateTime horaLlamada;
    private boolean atendida;

    //Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getRestauranteId() {
        return restauranteId;
    }
    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }
    public int getMesaId() {
        return mesaId;
    }
    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }
    public LocalDateTime getHoraLlamada() {
        return horaLlamada;
    }
    public void setHoraLlamada(LocalDateTime horaLlamada) {
        this.horaLlamada = horaLlamada;
    }
    public boolean isAtendida() {
        return atendida;
    }
    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    

   
}
