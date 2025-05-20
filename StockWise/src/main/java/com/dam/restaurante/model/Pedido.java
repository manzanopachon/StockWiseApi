package com.dam.restaurante.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.dam.restaurante.dto.PedidoDTO;

import jakarta.persistence.*;

@Entity
public class Pedido {

    public enum EstadoPedido {
        PENDIENTE,
        EN_PROCESO,
        FINALIZADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;

    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false)
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;


    private Integer numeroMesa;

    @Column(name = "codigo_pedido", nullable = false, unique = true)
    private String codigoPedido;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    @ManyToMany
    @JoinTable(
        name = "pedido_plato",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "plato_id")
    )
    private List<Plato> platos;

    public Pedido() {}

    @PrePersist
    public void prePersist() {
        if (this.codigoPedido == null || this.codigoPedido.isBlank()) {
            this.codigoPedido = generarCodigoPedido();
        }
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
        if (this.estadoPedido == null) {
            this.estadoPedido = EstadoPedido.PENDIENTE;
        }
        if (this.total == null) {
            this.total = calcularTotal();
        }
    }

    private double calcularTotal() {
        if (platos == null || platos.isEmpty()) return 0.0;
        return platos.stream()
                     .mapToDouble(Plato::getPrecio)
                     .sum();
    }

    public static String generarCodigoPedido() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder("P-");
        for (int i = 0; i < 6; i++) {
            codigo.append(chars.charAt(new Random().nextInt(chars.length())));
        }
        return codigo.toString();
    }

    public static Pedido fromDTO(PedidoDTO dto, Restaurante restaurante, List<Plato> platos) {
        Pedido pedido = new Pedido();
        pedido.setFechaHora(dto.getFechaHora() != null ? dto.getFechaHora() : LocalDateTime.now());
        pedido.setNumeroMesa(dto.getNumeroMesa());
        pedido.setRestaurante(restaurante);
        pedido.setPlatos(platos);
        return pedido;
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public EstadoPedido getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(EstadoPedido estadoPedido) { this.estadoPedido = estadoPedido; }

    public Integer getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(Integer numeroMesa) { this.numeroMesa = numeroMesa; }

    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }

    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }

    public List<Plato> getPlatos() { return platos; }
    public void setPlatos(List<Plato> platos) { this.platos = platos; }
}
