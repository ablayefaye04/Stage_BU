package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.TheseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TheseService {
    @Autowired
    private TheseRepository theseRepository;
    // Méthode pour récupérer une thèse par son ID
    public These getThesisById(Long id) {
        return theseRepository.findById(id).orElse(null); // Retourne la thèse si elle existe, sinon null
    }

    // Méthode pour mettre à jour une thèse
    public void updateThesis(Long id, These updatedThesis) {
        These these = theseRepository.findById(id).orElse(null);
        if (these != null) {
            these.setCote(updatedThesis.getCote());
            these.setTitre(updatedThesis.getTitre());
            these.setEtudiant(updatedThesis.getEtudiant());
            these.setEncadrant(updatedThesis.getEncadrant());
            these.setAnnee(updatedThesis.getAnnee());
            these.setExemplaires(updatedThesis.getExemplaires());
            theseRepository.save(these); // Sauvegarde la thèse mise à jour
        }
    }

    // Méthode pour supprimer une thèse
    public void deleteThesis(Long id) {
        theseRepository.deleteById(id); // Supprime la thèse par son ID
    }
}
