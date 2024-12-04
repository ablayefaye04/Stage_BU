package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DepartementRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.FiliereRepository;

import java.util.List;

@RestController  // Utilise @RestController pour retourner directement des réponses JSON
@RequestMapping("/memoires")  // Préfixe pour toutes les routes de ce contrôleur
public class FiliereController {

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private DepartementRepository departementRepository;

    // Endpoint pour récupérer les départements d'un UFR spécifique
    @GetMapping("/departements/ufr/{ufrId}")
    public List<Departement> getDepartementsByUfr(@PathVariable Long ufrId) {
        return departementRepository.findByUfrId(ufrId);  // Méthode qui récupère les départements par UFR
    }

    // Endpoint pour récupérer les filières d'un département spécifique
    @GetMapping("/filieres/departement/{departementId}")
    public List<Filiere> getFilieresByDepartement(@PathVariable Long departementId) {
        return filiereRepository.findByDepartementId(departementId);  // Méthode qui récupère les filières par Département
    }
}