package com.dam.restaurante.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dam.restaurante.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	@Query("SELECT p FROM Pedido p WHERE LOWER(p.codigoPedido) = LOWER(:codigo)")
	Optional<Pedido> buscarPorCodigoIgnorandoCase(@Param("codigo") String codigo);

    List<Pedido> findAllByRestauranteIdAndEstadoPedido(Long restauranteId, Pedido.EstadoPedido estadoPedido);

    List<Pedido> findAllByRestauranteId(Long restauranteId);
}
