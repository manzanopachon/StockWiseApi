package com.dam.restaurante.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import com.dam.restaurante.dto.PedidoDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaHora = LocalDateTime.now();

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



    // Getters y setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getNumeroMesa() {
		return numeroMesa;
	}

	public void setNumeroMesa(Integer numeroMesa) {
		this.numeroMesa = numeroMesa;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public List<Plato> getPlatos() {
		return platos;
	}

	public void setPlatos(List<Plato> platos) {
		this.platos = platos;
	}

	public String getCodigoPedido() {
		return codigoPedido;
	}

	public void setCodigoPedido(String codigoPedido) {
		this.codigoPedido = codigoPedido;
	}

	public static Pedido fromDTO(PedidoDTO dto, Restaurante restaurante, List<Plato> platos) {
	    Pedido pedido = new Pedido();
	    pedido.setNumeroMesa(dto.getNumeroMesa());
	    pedido.setRestaurante(restaurante);
	    pedido.setFechaHora(dto.getFechaHora() != null ? dto.getFechaHora() : LocalDateTime.now());
	    pedido.setPlatos(platos);
	    pedido.setCodigoPedido(generarCodigoPedido());
	    return pedido;
	}
	
	public static String generarCodigoPedido() {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    StringBuilder codigo = new StringBuilder("P-");
	    for (int i = 0; i < 6; i++) {
	        codigo.append(chars.charAt(new Random().nextInt(chars.length())));
	    }
	    return codigo.toString();
	}
    
}