package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Memoire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID unique auto-généré
    private Long id;

    @Column(name = "cote", nullable = false)
    private String cote;
    private String titre;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "encadrant_id")
    private Encadrant encadrant;
    private int annee;
    private int exemplaires;  // Le nombre d'exemplaires
    @Enumerated(EnumType.STRING) // Sauvegarde le nom de l'enum en texte
    private TypeMemoire type; // Nouveau champ pour le type de mémoire
    @ManyToOne
    @JoinColumn(name = "filiere_id")
    @JsonBackReference
    private Filiere filiere;

    // Champs ajoutés pour les accès rapides à `Departement` et `Ufr`s
    @ManyToOne
    @JoinColumn(name = "departement_id")
    @JsonBackReference
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "ufr_id")
    @JsonBackReference
    private Ufr ufr;
    @Transient
    private int numero;

}
