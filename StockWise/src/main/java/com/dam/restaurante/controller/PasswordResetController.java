package com.dam.restaurante.controller;

import com.dam.restaurante.model.Empleado;
import com.dam.restaurante.model.PasswordResetToken;
import com.dam.restaurante.repository.EmpleadoRepository;
import com.dam.restaurante.repository.PasswordResetTokenRepository;
import com.dam.restaurante.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Solicitar cambio de contraseña
    @PostMapping("/forgot-password")
    public ResponseEntity<?> solicitarCambio(@RequestParam String correo) {
        Optional<Empleado> empleadoOpt = empleadoRepository.findByCorreoIgnoreCase(correo);
        System.out.println("📩 Solicitando recuperación para: " + correo);
        System.out.println("¿Existe? " + empleadoRepository.findByCorreoIgnoreCase(correo).isPresent());


        if (empleadoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No existe ningún empleado con ese correo.");
        }

        String token = emailService.generarToken(correo);

        String enlace = "https://manzanopachon.github.io/reset-password/reset-password.html?token=" + token;


        String html = "<h2>Recuperación de contraseña</h2>" +
                "<p>Haz clic en el siguiente botón para restablecer tu contraseña:</p>" +
                "<a href=\"" + enlace + "\" style=\"padding: 10px 20px; background-color: #4CAF50; color: white; " +
                "text-decoration: none; border-radius: 5px;\">Cambiar contraseña</a><br><br>" +
                "<small>Si no solicitaste este cambio, ignora este correo.</small>";

        emailService.enviarCorreoRecuperacion(correo, html);

        return ResponseEntity.ok("Correo de recuperación enviado correctamente.");
    }

    // 2. Restablecer contraseña con token válido
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String nuevaClave
    ) {
        Optional<PasswordResetToken> prtOpt = tokenRepository.findByToken(token);

        if (prtOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Token inválido.");
        }

        PasswordResetToken prt = prtOpt.get();

        if (prt.getExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(410).body("El token ha expirado.");
        }

        Optional<Empleado> empleadoOpt = empleadoRepository.findByCorreo(prt.getEmail());

        if (empleadoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Empleado no encontrado.");
        }

        Empleado empleado = empleadoOpt.get();
        empleado.setContraseña(passwordEncoder.encode(nuevaClave));
        empleadoRepository.save(empleado);

        tokenRepository.delete(prt);

        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }

    @GetMapping("/debug-correos")
    public List<String> listarCorreos() {
        return empleadoRepository.findAll().stream()
            .map(Empleado::getCorreo)
            .toList();
    }
}
