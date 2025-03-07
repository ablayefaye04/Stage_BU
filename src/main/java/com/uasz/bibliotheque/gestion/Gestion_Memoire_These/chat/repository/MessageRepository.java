package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.repository;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}