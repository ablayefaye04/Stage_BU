package com.uasz.bibliotheque.gestion.Gestion_Memoire_These;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.MemoireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionMemoireTheseApplication implements CommandLineRunner {
	@Autowired
	private MemoireService memoireService; // Injection du service

	public static void main(String[] args) {
		SpringApplication.run(GestionMemoireTheseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Insertion d'une nouvelle mémoire
		memoireService.ajouterMemoire(
				"UFR Sciences Santé (SS)",                   // Nom de l'UFR
				"Santé",               // Nom du département
				"Médecine",             // Nom de la filière
				"MASTER",                    // Type de mémoire (par exemple : LICENCE, MASTER, DOCTORAT)
				"Organes Vitaux", // Titre de la mémoire
				2023,                         // Année
				1,                            // Nombre d'exemplaires
				"Diallo",                     // Nom de l'étudiant
				"Massata",                   // Prénom de l'étudiant
				"Siby",                    // Prénom de l'encadrant
				"SECK"                        // Nom de l'encadrant
		);

		System.out.println("Mémoire ajoutée avec succès !");

		// Insertion d'une nouvelle mémoire - Exemple 2
		memoireService.ajouterMemoire(
				"UFR Sciences et Techniques (ST)",                   // Nom de l'UFR
				"Informatique",                               // Nom du département
				"Génie logiciel",                                   // Nom de la filière
				"MASTER",                                     // Type de mémoire
				"Optimisation des algorithmes de traitement de données massives", // Titre de la mémoire
				2012,                                         // Année
				3,                                            // Nombre d'exemplaires
				"Dieng",                                      // Nom de l'étudiant
				"Alioune",                                    // Prénom de l'étudiant
				"Boubacar",                                   // Prénom de l'encadrant
				"Diouf"                                       // Nom de l'encadrant
		);
		System.out.println("Mémoire ajoutée avec succès !");

		// Insertion d'une nouvelle mémoire - Exemple 3
		memoireService.ajouterMemoire(
				"UFR Lettres, Art, Sciences Humaines (LASHU)",                   // Nom de l'UFR
				"Histoire",                               // Nom du département
				"Archéologie",                  // Nom de la filière
				"LICENCE",                                   // Type de mémoire
				"Développement d'un modèle prédictif pour la détection des maladies", // Titre de la mémoire
				2022,                                         // Année
				1,                                            // Nombre d'exemplaires
				"Ba",                                         // Nom de l'étudiant
				"Moussa",                                     // Prénom de l'étudiant
				"Omar",                                       // Prénom de l'encadrant
				"Ndiaye"                                      // Nom de l'encadrant
		);
		System.out.println("Mémoire ajoutée avec succès !");

		// Insertion d'une nouvelle mémoire - Exemple 4
		memoireService.ajouterMemoire(
				"UFR Sciences Economiques et Sociales (SES)",                   // Nom de l'UFR
				"Management Informatique des Organisations(MIO)",                               // Nom du département
				"Méthodes Informatiques Appliquées à la Gestion (MIAGE)",                         // Nom de la filière
				"LICENCE",                                    // Type de mémoire
				"Mise en place d'une architecture réseau pour entreprise", // Titre de la mémoire
				2022,                                         // Année
				4,                                            // Nombre d'exemplaires
				"Mendy",                                      // Nom de l'étudiant
				"Amadou",                                     // Prénom de l'étudiant
				"Salif",                                      // Prénom de l'encadrant
				"Sene"                                        // Nom de l'encadrant
		);
		System.out.println("Mémoire ajoutée avec succès !");

	}
}
