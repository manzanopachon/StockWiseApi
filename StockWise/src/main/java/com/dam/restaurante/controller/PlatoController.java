package com.dam.restaurante.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dam.restaurante.dto.PlatoDTO;
import com.dam.restaurante.dto.PlatoIngredienteDTO;
import com.dam.restaurante.service.PlatoService;

@RestController
@RequestMapping("/api/platos")
@CrossOrigin(origins = "*")
public class PlatoController {

    @Autowired
    private PlatoService platoService;
    //Listar todos los platos
    @GetMapping
    public List<PlatoDTO> listarPlatos() {
        return platoService.obtenerTodos();
    }
    //Listar todos los platos de un restaurante segun el restaurante id
    @GetMapping("/restaurante/{restauranteId}")
    public List<PlatoDTO> listarPorRestaurante(@PathVariable Long restauranteId) {
        return platoService.obtenerPorRestauranteId(restauranteId);
    }
    //Obtener un plato segund su id
    @GetMapping("/{id}")
    public PlatoDTO obtenerPlato(@PathVariable Long id) {
        return platoService.obtenerPorId(id);
    }

    //Crear un plato
    @PostMapping
    public ResponseEntity<PlatoDTO> crearPlato(@RequestBody PlatoDTO platoDTO) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(platoDTO);
    }
    //Actualizar el plato segun su id
    @PutMapping("/{id}")
    public PlatoDTO actualizarPlato(@PathVariable Long id, @RequestBody PlatoDTO dto) {
        return platoService.actualizarPlato(id, dto);
    }
    //Eliminar un plato segun su id
    @DeleteMapping("/{id}")
    public void eliminarPlato(@PathVariable Long id) {
        platoService.eliminarPlato(id);
    }

    // 1. Asignar múltiples ingredientes a un plato
    @PostMapping("/{platoId}/ingredientes")
    public ResponseEntity<?> asignarIngredientes(
            @PathVariable Long platoId,
            @RequestBody Map<String, Double> ingredientesConCantidad) {
        try {
            Map<Long, Double> ingredientesConvertidos = ingredientesConCantidad.entrySet().stream()
                .collect(Collectors.toMap(
                    e -> Long.parseLong(e.getKey()),
                    Map.Entry::getValue
                ));

            platoService.asignarIngredientesAPlato(platoId, ingredientesConvertidos);
            return ResponseEntity.ok("Ingredientes asignados correctamente.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al asignar ingredientes: " + e.getMessage());
        }
    }


    // 2. Modificar cantidad de un ingrediente específico en un plato
    @PutMapping("/{platoId}/ingredientes/{ingredienteId}")
    public ResponseEntity<?> modificarCantidadIngrediente(
            @PathVariable Long platoId,
            @PathVariable Long ingredienteId,
            @RequestBody Double nuevaCantidad) {
        platoService.modificarCantidadIngrediente(platoId, ingredienteId, nuevaCantidad);
        return ResponseEntity.ok("Cantidad modificada correctamente.");
    }

    // 3. Eliminar un ingrediente de un plato
    @DeleteMapping("/{platoId}/ingredientes/{ingredienteId}")
    public ResponseEntity<?> eliminarIngredienteDePlato(
            @PathVariable Long platoId,
            @PathVariable Long ingredienteId) {
        platoService.eliminarIngredienteDePlato(platoId, ingredienteId);
        return ResponseEntity.ok("Ingrediente eliminado del plato.");
    }

    // 4. Obtener ingredientes de un plato
    @GetMapping("/{platoId}/ingredientes")
    public ResponseEntity<List<PlatoIngredienteDTO>> obtenerIngredientesDePlato(@PathVariable Long platoId) {
        List<PlatoIngredienteDTO> ingredientesDTO = platoService.obtenerIngredientesDePlatoDTO(platoId);
        return ResponseEntity.ok(ingredientesDTO);
    }

}
