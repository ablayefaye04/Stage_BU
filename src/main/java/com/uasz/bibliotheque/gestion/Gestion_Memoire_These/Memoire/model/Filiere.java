package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false )
    private Departement departement;

    @OneToMany(mappedBy = "filiere")
    private List<Memoire> memoires;  // Association avec Memoire
}
