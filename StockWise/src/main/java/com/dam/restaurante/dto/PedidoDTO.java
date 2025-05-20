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

    // Solo para respuesta (no debe ser seteado por el cliente)
    private LocalDateTime fechaHora;
    private String codigoPedido;
    private String estadoPedido;

    public PedidoDTO() {}

    // Constructor de respuesta
    public PedidoDTO(Pedido pedido) {
        this.numeroMesa = pedido.getNumeroMesa();
        this.restauranteId = pedido.getRestaurante().getId();
        this.fechaHora = pedido.getFechaHora();
        this.codigoPedido = pedido.getCodigoPedido();
        this.estadoPedido = pedido.getEstadoPedido().name();
        this.platos = pedido.getPlatos()
                            .stream()
                            .map(Plato::getId)
                            .collect(Collectors.toList());
    }

    // Getters y setters

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

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
}
