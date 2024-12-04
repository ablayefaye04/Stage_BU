package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DepartementRepository;

import java.util.List;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    // Récupérer tous les départements
    public List<Departement> findAllDepartements() {
        return departementRepository.findAll();
    }

    // Récupérer les départements par UFR
    public List<Departement> findDepartementsByUfr(Long ufrId) {
        return departementRepository.findByUfrId(ufrId);  // Cette méthode devra être définie dans le repository
    }
}
