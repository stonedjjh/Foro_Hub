package com.aluracurso.foro_hub.aplication.service;

import com.aluracurso.foro_hub.domain.topico.Topico;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyNewTopic(Topico topico) {
        System.out.println("Notificando sobre el nuevo tópico: " + topico.getTitulo());
        // Aquí podrías agregar la lógica real para enviar un correo electrónico,
        // una notificación push o un mensaje a una cola de mensajes.
        // Por ejemplo:
        // emailService.sendEmail(topico.getAutor(), "Nuevo Tópico creado", "Se ha creado el tópico: " + topico.getTitulo());
    }
}

