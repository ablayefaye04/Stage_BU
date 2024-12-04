package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Etudiant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;

    // Méthode pour obtenir le nom complet
    public String getNomComplet() {
        return this.nom + " " + this.prenom;
    }
}
