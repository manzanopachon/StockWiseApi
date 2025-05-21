package com.dam.restaurante.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.dam.restaurante.model.Pedido;
import com.dam.restaurante.model.Plato;

public class PedidoDTO {

	private Long id;
    private Integer numeroMesa;
    private Long restauranteId;
    private List<Long> platos;
    private List<PlatoInfo> detallesPlatos;

    private Double total;
    private LocalDateTime fechaHora;
    private String codigoPedido;
    private String estadoPedido;

    public PedidoDTO() {}

    public PedidoDTO(Pedido pedido) {
    	 this.id = pedido.getId();
        this.numeroMesa = pedido.getNumeroMesa();
        this.restauranteId = pedido.getRestaurante().getId();
        this.fechaHora = pedido.getFechaHora();
        this.total = pedido.getTotal();
        this.codigoPedido = pedido.getCodigoPedido();
        this.estadoPedido = pedido.getEstadoPedido().name();
        this.platos = pedido.getPlatos()
                            .stream()
                            .map(Plato::getId)
                            .collect(Collectors.toList());

        this.detallesPlatos = pedido.getPlatos()
                .stream()
                .map(plato -> new PlatoInfo(plato.getNombre(), plato.getPrecio()))
                .collect(Collectors.toList());
    }

    // Getters y setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
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

    public List<PlatoInfo> getDetallesPlatos() {
        return detallesPlatos;
    }

    public void setDetallesPlatos(List<PlatoInfo> detallesPlatos) {
        this.detallesPlatos = detallesPlatos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
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
