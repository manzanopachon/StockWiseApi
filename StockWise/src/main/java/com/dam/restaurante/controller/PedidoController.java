package com.dam.restaurante.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dam.restaurante.dto.PedidoDTO;
import com.dam.restaurante.model.*;
import com.dam.restaurante.repository.*;

import jakarta.transaction.Transactional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private PlatoRepository platoRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @PostMapping("/crear")
    @Transactional
    public ResponseEntity<?> crearPedido(@RequestBody PedidoDTO dto) {
        Optional<Restaurante> restauranteOpt = restauranteRepository.findById(dto.getRestauranteId());
        if (restauranteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Restaurante no encontrado");
        }
        Restaurante restaurante = restauranteOpt.get();

        List<Long> idsPlatos = dto.getPlatos();
        List<Plato> platos = platoRepository.findAllById(idsPlatos);

        if (platos.isEmpty()) {
            return ResponseEntity.badRequest().body("No se encontró ningún plato válido");
        }

        Pedido pedido = Pedido.fromDTO(dto, restaurante, platos);

        // 👇 Asegúrate de forzar el código aquí
        String codigoGenerado = Pedido.generarCodigoPedido();
        pedido.setCodigoPedido(codigoGenerado);
        System.out.println(">> CÓDIGO GENERADO (controller): " + codigoGenerado);

        Map<Ingrediente, Double> ingredientesARestar = new HashMap<>();
        for (Plato plato : platos) {
            for (PlatoIngrediente pi : plato.getIngredientes()) {
                ingredientesARestar.merge(
                    pi.getIngrediente(),
                    pi.getCantidadNecesaria(),
                    Double::sum
                );
            }
        }

        for (Map.Entry<Ingrediente, Double> entry : ingredientesARestar.entrySet()) {
            Ingrediente ingrediente = entry.getKey();
            double cantidadRestar = entry.getValue();

            if (ingrediente.getCantidadStock() < cantidadRestar) {
                return ResponseEntity.badRequest()
                    .body("No hay suficiente stock para " + ingrediente.getNombre());
            }

            ingrediente.setCantidadStock(ingrediente.getCantidadStock() - cantidadRestar);
            ingredienteRepository.save(ingrediente);
        }

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return ResponseEntity.ok(new PedidoDTO(pedidoGuardado));
    }


    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<PedidoDTO> buscarPorCodigo(@PathVariable String codigo) {
        Pedido pedido = pedidoRepository.findByCodigoPedido(codigo);
        if (pedido == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new PedidoDTO(pedido));
    }
}
