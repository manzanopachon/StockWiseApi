package com.dam.restaurante.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dam.restaurante.dto.PedidoDTO;
import com.dam.restaurante.model.Pedido;
import com.dam.restaurante.model.Pedido.EstadoPedido;
import com.dam.restaurante.service.PedidoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    //Crear pedido
    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestBody PedidoDTO dto) {
        try {
            Pedido nuevoPedido = pedidoService.crearPedidoDesdeDTO(dto);
            return ResponseEntity.ok(Map.of("codigoPedido", nuevoPedido.getCodigoPedido()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    //Consultar pedido por código
    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        try {
            Pedido pedido = pedidoService.obtenerPedidoPorCodigo(codigo);
            return ResponseEntity.ok(new PedidoDTO(pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Obtener solo el estado del pedido
    @GetMapping("/estado/{codigo}")
    public ResponseEntity<?> obtenerEstadoPorCodigo(@PathVariable String codigo) {
        try {
            Pedido pedido = pedidoService.obtenerPedidoPorCodigo(codigo);
            return ResponseEntity.ok(pedido.getEstadoPedido().name());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Obtener todos los pedidos pendientes por restaurante (empleado)
    @GetMapping("/restaurante/{id}/pendientes")
    public ResponseEntity<?> obtenerPedidosPendientes(@PathVariable Long id) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorRestauranteYEstado(id, EstadoPedido.PENDIENTE);
        List<PedidoDTO> dtos = pedidos.stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/restaurante/{id}/todos")
    public ResponseEntity<?> obtenerTodosLosPedidos(@PathVariable Long id) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorRestaurante(id);
        List<PedidoDTO> dtos = pedidos.stream()
                .map(PedidoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    //Confirmar pedido y descontar ingredientes (empleado)
    @PostMapping("/confirmar/{id}")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long id) {
        try {
            pedidoService.confirmarPedido(id);
            return ResponseEntity.ok("Pedido confirmado y stock actualizado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Cambiar estado manualmente (empleado)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        try {
            pedidoService.actualizarEstado(id, estado);

            // Si el estado es FINALIZADO, aplicar la lógica de stock
            if (estado == EstadoPedido.FINALIZADO) {
                pedidoService.confirmarPedido(id); // Esto ya descuenta ingredientes
            }

            Pedido pedidoActualizado = pedidoService.obtenerPorId(id);
            return ResponseEntity.ok(new PedidoDTO(pedidoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
