package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.model.Message;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Message envoyerMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    /**
     * Récupérer un message par son ID.
     */
    public Message getMessageById(Long id) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        return messageOptional.orElse(null); // Retourne null si le message n'est pas trouvé
    }

    /**
     * Modifier un message existant.
     */
    public void modifierMessage(Message message) {
        if (messageRepository.existsById(message.getId())) {
            messageRepository.save(message); // Met à jour le message
        } else {
            throw new RuntimeException("Message non trouvé !");
        }
    }

    /**
     * Supprimer un message par son ID.
     */
    public void supprimerMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id); // Supprime le message
        } else {
            throw new RuntimeException("Message non trouvé !");
        }
    }
}
