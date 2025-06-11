package com.dam.restaurante.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import com.dam.restaurante.model.PasswordResetToken;
import com.dam.restaurante.repository.PasswordResetTokenRepository;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public void enviarDatosRegistro(String contenidoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo("jmauriciomp22@gmail.com");
            helper.setSubject("📥 Nuevo empleado registrado en StockWise");
            helper.setText(contenidoHtml, true); // Activamos HTML

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo");
        }
    }

    public void enviarCorreoRecuperacion(String correoDestino, String contenidoHtml) {
    try {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(correoDestino); // ← ¡Esto lo hace dinámico!
        helper.setSubject("🔐 Restablecer contraseña - StockWise"); // ← Asunto correcto
        helper.setText(contenidoHtml, true);

        mailSender.send(mensaje);
    } catch (MessagingException e) {
        e.printStackTrace();
        throw new RuntimeException("Error al enviar el correo");
    }
}

    public String generarToken(String email) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiration(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);
        return token;
    }

}
