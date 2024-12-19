package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.controller;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.EmailService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    /**
     * Affiche la page de connexion.
     */
    @RequestMapping(value = "/login")
    public String index() {
        return "login";
    }

    /**
     * Redirige l'utilisateur connecté vers le tableau de bord approprié en fonction de son rôle.
     */
    @RequestMapping("/")
    public String login(Principal principal) {
        if (principal != null) {
            Utilisateur utilisateur = utilisateurService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null && utilisateur.getRoles() != null && !utilisateur.getRoles().isEmpty()) {
                String role = utilisateur.getRoles().get(0).getRole();
                switch (role) {
                    case "Responsable":
                        return "redirect:/memoires/liste";
                    case "Stager":
                        return "redirect:/memoires/liste";
                    default:
                        return "redirect:/login?error=role_inconnu";
                }
            }
        }
        return "redirect:/login";
    }


    /**
     * Affiche la page de succès après une inscription réussie.
     */
    @GetMapping("/success")
    public String afficherPageSucces() {
        return "success";
    }

    /**
     * Gère l'inscription d'un nouvel utilisateur.
     */
    @PostMapping("/ajouterUtilisateur")
    public String ajouterUtilisateur(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String role) {

        // Vérifier si l'utilisateur existe déjà
        if (utilisateurService.recherche_Utilisateur(username) != null) {
            return "redirect:/error"; // Redirection en cas d'email déjà existant
        }
        // Créer et configurer un nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(username);
        utilisateur.setPassword(passwordEncoder.encode(password)); // Encode le mot de passe
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        // Récupérer ou créer le rôle
        Role roleEntity = utilisateurService.recherche_role(role);
        if (roleEntity == null) {
            roleEntity = utilisateurService.ajouter_role(new Role(role));
        }

        // Associer le rôle à l'utilisateur
        utilisateur.getRoles().add(roleEntity);

        // Sauvegarder l'utilisateur
        utilisateurService.ajouter_Utilisateur(utilisateur);

        // Envoyer un e-mail avec les informations de connexion
        emailService.sendAccountCreationEmail(username, username, password);

        return "redirect:/listeResponsables"; // Redirection après succès
    }



    /**
     * Gère la déconnexion de l'utilisateur et redirige vers la page de connexion.
     */
    @RequestMapping(value = "/logout")
    public String logOutAndRedirectToLoginPage(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout=true";
    }

    /**
     * Affiche la liste des utilisateurs.
     */
    @GetMapping("/listeResponsables")
    public String listeResponsables(Model model) {
        // Récupère la liste des responsables depuis le service
        List<Utilisateur> responsables = utilisateurService.listUtilisateur();
        model.addAttribute("listeResponsables", responsables); // Transfert des données au modèle
        return "Responsable"; // Assurez-vous que ce nom correspond au fichier Thymeleaf (ex: responsables.html)
    }

    @PostMapping("/ajouterResponsable")
    public String ajouterResponsable(Utilisateur user){
        utilisateurService.ajouter_Utilisateur(user);
        return "redirect:/listeResponsables";
    }

    @PostMapping("/modifierResponsable")
    public String modifierResponsable(@ModelAttribute Utilisateur utilisateur) {
        Utilisateur User = utilisateurRepository.findById(utilisateur.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Mettre à jour uniquement les champs modifiables
        User.setNom(utilisateur.getNom());
        User.setPrenom(utilisateur.getPrenom());
        User.setUsername(utilisateur.getUsername());
        utilisateurService.modifierUtilisateur(User);
        return "redirect:/listeResponsables";
    }


    @PostMapping("/supprimerResponsable")
    public  String supprimer_Etudiant(Utilisateur user){
        utilisateurService.supprimerResponsable(user);
        return "redirect:/listeResponsables" ;
    }

    @GetMapping("/error_email")
    public String AfficherErreurEmail(){
        return "error";
    }

}
