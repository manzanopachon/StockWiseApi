package com.dam.restaurante.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dam.restaurante.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByCodigoPedido(String codigoPedido);
    List<Pedido> findAllByRestauranteIdAndEstadoPedido(Long restauranteId, Pedido.EstadoPedido estadoPedido);

}
