package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.controller;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.model.Notification;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Afficher les notifications non lues
    @GetMapping
    public String afficherNotifications(Model model) {
        List<Notification> notifications = notificationService.getNotificationNonLue();
        model.addAttribute("notifications", notifications);
        return "notifications"; // Vue Thymeleaf pour afficher les notifications
    }

    // Marquer une notification comme lue
    @PostMapping("/lire/{id}")
    public String marquerCommeLue(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return "redirect:/notifications"; // Redirection vers la liste des notifications
    }

    @PostMapping("/notifications/supprimer/{id}")
    public String supprimerNotification(@PathVariable Long id) {
        notificationService.supprimerNotification(id);
        return "redirect:/notifications";
    }



}
