package com.dam.restaurante.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.dam.restaurante.dto.PedidoDTO;

import jakarta.persistence.*;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private Integer numeroMesa;

    @Column(name = "codigo_pedido", unique = true)
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

    // Constructor vacío requerido por JPA
    public Pedido() {}

    
    @PrePersist
    private void asignarCodigoSiFalta() {
        if (this.codigoPedido == null || this.codigoPedido.isBlank()) {
            this.codigoPedido = generarCodigoPedido();
        }
    }



    // Lógica de generación del código
    public static String generarCodigoPedido() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codigo = new StringBuilder("P-");
        for (int i = 0; i < 6; i++) {
            codigo.append(chars.charAt(new Random().nextInt(chars.length())));
        }
        return codigo.toString();
    }

    // Método para crear Pedido desde DTO
    public static Pedido fromDTO(PedidoDTO dto, Restaurante restaurante, List<Plato> platos) {
        Pedido pedido = new Pedido();
        pedido.codigoPedido = generarCodigoPedido(); // directo
        pedido.fechaHora = dto.getFechaHora() != null ? dto.getFechaHora() : LocalDateTime.now();
        pedido.numeroMesa = dto.getNumeroMesa();
        pedido.restaurante = restaurante;
        pedido.platos = platos;
        return pedido;
    }




    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Integer getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(Integer numeroMesa) { this.numeroMesa = numeroMesa; }

    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }

    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }

    public List<Plato> getPlatos() { return platos; }
    public void setPlatos(List<Plato> platos) { this.platos = platos; }
}
