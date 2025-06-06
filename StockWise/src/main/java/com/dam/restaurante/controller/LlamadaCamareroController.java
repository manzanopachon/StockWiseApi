package com.dam.restaurante.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dam.restaurante.model.LlamadaCamarero;
import com.dam.restaurante.repository.LlamadaRepository;
@RestController
@RequestMapping("/api/llamadas")
@CrossOrigin(origins = "*") // Permitir llamadas desde frontend
public class LlamadaCamareroController {

    @Autowired
    private LlamadaRepository llamadaRepository;

    @PostMapping("/crear")
    public ResponseEntity<?> crearLlamada(@RequestParam Long restauranteId, @RequestParam int mesaId) {
        LlamadaCamarero llamada = new LlamadaCamarero();
        llamada.setRestauranteId(restauranteId);
        llamada.setMesaId(mesaId);
        llamada.setHoraLlamada(LocalDateTime.now());
        llamada.setAtendida(false);
        llamadaRepository.save(llamada);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pendientes")
    public List<LlamadaCamarero> obtenerLlamadasPendientes() {
        return llamadaRepository.findByAtendidaFalse();
    }

    @PostMapping("/atender/{id}")
    public ResponseEntity<?> marcarComoAtendida(@PathVariable Long id) {
        llamadaRepository.findById(id).ifPresent(llamada -> {
            llamada.setAtendida(true);
            llamadaRepository.save(llamada);
        });
        return ResponseEntity.ok().build();
    }
}