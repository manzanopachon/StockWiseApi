package com.dam.restaurante.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dam.restaurante.dto.PedidoDTO;
import com.dam.restaurante.model.Ingrediente;
import com.dam.restaurante.model.Pedido;
import com.dam.restaurante.model.Pedido.EstadoPedido;
import com.dam.restaurante.model.PedidoDetalle;
import com.dam.restaurante.model.Plato;
import com.dam.restaurante.model.PlatoIngrediente;
import com.dam.restaurante.model.Restaurante;
import com.dam.restaurante.repository.IngredienteRepository;
import com.dam.restaurante.repository.PedidoDetalleRepository;
import com.dam.restaurante.repository.PedidoRepository;
import com.dam.restaurante.repository.PlatoRepository;
import com.dam.restaurante.repository.RestauranteRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;


    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;
    
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    // ✅ Crear un pedido a partir de DTO (cliente gestionado internamente)
    public Pedido crearPedidoDesdeDTO(PedidoDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
            .orElseThrow(() -> new RuntimeException("Restaurante no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setFechaHora(dto.getFechaHora() != null ? dto.getFechaHora() : LocalDateTime.now());
        pedido.setNumeroMesa(dto.getNumeroMesa());
        pedido.setRestaurante(restaurante);
        pedido.setCodigoPedido(Pedido.generarCodigoPedido());
        pedido.setEstadoPedido(Pedido.EstadoPedido.PENDIENTE);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // ✅ Agrupar platos por ID para contar cantidades
        var mapaCantidad = dto.getPlatos().stream()
            .collect(Collectors.groupingBy(id -> id, Collectors.counting()));

        double total = 0.0;

        for (var entry : mapaCantidad.entrySet()) {
            Long platoId = entry.getKey();
            int cantidad = entry.getValue().intValue();

            Plato plato = platoRepository.findById(platoId)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado: " + platoId));

            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedidoGuardado);
            detalle.setPlato(plato);
            detalle.setCantidad(cantidad);
            detalle.setPrecio(plato.getPrecio());

            pedidoDetalleRepository.save(detalle);

            total += plato.getPrecio() * cantidad;
        }

        pedidoGuardado.setTotal(total);
        return pedidoRepository.save(pedidoGuardado);
    }



    // ✅ Obtener pedido por código
    public Pedido obtenerPedidoPorCodigo(String codigoPedido) {
        return pedidoRepository.buscarPorCodigoIgnorandoCase(codigoPedido)

            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    // ✅ Confirmar pedido (ejemplo de paso de estado y lógica de stock)
    public void confirmarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<PedidoDetalle> detalles = pedidoDetalleRepository.findAllByPedido(pedido);

        for (PedidoDetalle detalle : detalles) {
            Plato plato = detalle.getPlato();
            List<PlatoIngrediente> ingredientes = plato.getIngredientes();

            for (PlatoIngrediente platoIngrediente : ingredientes) {
                Ingrediente ingrediente = platoIngrediente.getIngrediente();
                double cantidadUsada = platoIngrediente.getCantidadNecesaria() * detalle.getCantidad();

                if (ingrediente.getCantidadStock() >= cantidadUsada) {
                    ingrediente.setCantidadStock(ingrediente.getCantidadStock() - cantidadUsada);
                    ingredienteRepository.save(ingrediente);
                } else {
                    throw new RuntimeException("No hay suficiente stock de " + ingrediente.getNombre());
                }
            }
        }

        // Actualizamos estado
        //pedido.setEstadoPedido(Pedido.EstadoPedido.EN_PROCESO);
        pedidoRepository.save(pedido);
    }
    
    public void actualizarEstado(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstadoPedido(nuevoEstado);
        pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerPedidosPorRestauranteYEstado(Long restauranteId, EstadoPedido estado) {
        return pedidoRepository.findAllByRestauranteIdAndEstadoPedido(restauranteId, estado);
    }
    
    public List<Pedido> obtenerPedidosPorRestaurante(Long restauranteId) {
        return pedidoRepository.findAllByRestauranteId(restauranteId);
    }


}
