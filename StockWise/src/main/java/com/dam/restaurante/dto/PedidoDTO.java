package com.dam.restaurante.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.dam.restaurante.model.Pedido;
import com.dam.restaurante.model.Plato;

public class PedidoDTO {

    private Integer numeroMesa;
    private Long restauranteId;
    private List<Long> platos;
    private LocalDateTime fechaHora;
    private String codigoPedido;

    // Constructor completo
    public PedidoDTO(Integer numeroMesa, Long restauranteId, List<Long> platos, LocalDateTime fechaHora, String codigoPedido) {
        this.numeroMesa = numeroMesa;
        this.restauranteId = restauranteId;
        this.platos = platos;
        this.fechaHora = fechaHora;
        this.codigoPedido = codigoPedido;
    }

    // Constructor vacío (necesario para @RequestBody en controladores)
    public PedidoDTO() {}

    // Constructor para convertir Pedido a DTO
    public PedidoDTO(Pedido pedido) {
        this.numeroMesa = pedido.getNumeroMesa();
        this.restauranteId = pedido.getRestaurante().getId();
        this.fechaHora = pedido.getFechaHora();
        this.codigoPedido = pedido.getCodigoPedido();
        this.platos = pedido.getPlatos()
                            .stream()
                            .map(Plato::getId)
                            .collect(Collectors.toList());
    }

    // Getters y Setters
    public Integer getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(Integer numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public List<Long> getPlatos() {
        return platos;
    }

    public void setPlatos(List<Long> platos) {
        this.platos = platos;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }
}
