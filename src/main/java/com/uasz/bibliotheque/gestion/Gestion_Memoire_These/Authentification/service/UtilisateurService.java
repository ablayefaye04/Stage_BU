package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service;



import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.RoleRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UtilisateurService {

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Utilisateur ajouter_Utilisateur(Utilisateur user){
        utilisateurRepository.save(user);
        return user;
    }

    public Role ajouter_role(Role role){
        roleRepository.save(role);
        return  role;
    }

    public void ajouter_UtilisateurRoles(Utilisateur user1, Role role) {
        Utilisateur user = utilisateurRepository.findUtilisateurByUsername(user1.getUsername());
        if (user == null) {
            throw new RuntimeException("Utilisateur introuvable : " + user1.getUsername());
        }

        Role profile = roleRepository.findRoleByRole(role.getRole());
        if (profile == null) { // Créer le rôle s'il n'existe pas
            profile = ajouter_role(role);
        }

        // Initialiser les rôles s'ils sont null
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        // Ajout du rôle à l'utilisateur
        if (!user.getRoles().contains(profile)) {
            user.getRoles().add(profile);
        }

        utilisateurRepository.save(user); // Sauvegarder les changements
    }


    public Utilisateur recherche_Utilisateur(String username){
        Utilisateur user = utilisateurRepository.findUtilisateurByUsername(username);
        return user;
    }

    public List<Utilisateur> listUtilisateur(){ return  utilisateurRepository.findAll() ;}

    public void supprimerResponsable(Utilisateur user){
        utilisateurRepository.delete(user);
    }

    public void modifierUtilisateur(Utilisateur user){
        utilisateurRepository.save(user);
    }
    // Recherche d'un rôle par nom
    public Role recherche_role(String roleName) {
        return roleRepository.findRoleByRole(roleName);
    }

}
