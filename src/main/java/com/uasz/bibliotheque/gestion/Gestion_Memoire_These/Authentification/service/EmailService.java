package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendAccountCreationEmail(String toEmail, String email, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Création de votre compte");
            message.setText("Bonjour,\n\nVotre compte a été créé avec succès.\n\n" +
                    "Voici vos informations de connexion :\n" +
                    "Email : " + email + "\n" +
                    "Mot de passe : " + password + "\n\n" +
                    "Veuillez garder ces informations en sécurité.\n\nCordialement,\nL'équipe.");

            mailSender.send(message);

        }catch (MailException e){
            // Gérer l'erreur sans interrompre l'application
            System.err.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }

    }
}
