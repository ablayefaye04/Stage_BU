package com.uasz.bibliotheque.gestion.Gestion_Memoire_These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DepartementRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.EcoleDoctoratRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.FiliereRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.UfrRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.MemoireService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class GestionMemoireTheseApplication implements CommandLineRunner {

	@Autowired
	private UfrRepository ufrRepository;

	@Autowired
	private EcoleDoctoratRepository ecoleDoctoratRepository;

	@Autowired
	private DepartementRepository departementRepository;

	@Autowired
	private FiliereRepository filiereRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private UtilisateurService utilisateurService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(GestionMemoireTheseApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			// Vérification et ajout des UFR si nécessaire
			insertUfr("UFR Sciences et Techniques (ST)");
			insertUfr("UFR Lettres, Art, Sciences Humaines (LASHU)");
			insertUfr("UFR Sciences Economiques et Sociales (SES)");
			insertUfr("UFR Sciences Santé (SS)");

			// Vérification et ajout des écoles doctorales
			insertEcoleDoctorat("Ecole Doctorat Sciences, Technologies et Ingénierie (ED-STI)", "UFR Sciences et Techniques (ST)");
			insertEcoleDoctorat("Ecole Doctorat Espaces, Sociétés et Humanités (ED-ESH)", "UFR Lettres, Art, Sciences Humaines (LASHU)");

			// Insérer les départements et les filières ici...
		};
	}

	private void insertUfr(String nom) {
		if (!ufrRepository.existsByNom(nom)) {
			Ufr ufr = new Ufr(nom);
			ufrRepository.save(ufr);
		}
	}

	private void insertEcoleDoctorat(String nom, String ufrNom) {
		Ufr ufr = ufrRepository.findByNom(ufrNom).orElse(null);
		if (ufr != null && !ecoleDoctoratRepository.existsByNom(nom)) {
			EcoleDoctorat ecoleDoctorat = new EcoleDoctorat(nom, ufr);
			ecoleDoctoratRepository.save(ecoleDoctorat);
		}
	}

	private void insertDepartement(String nom, String ufrNom) {
		Ufr ufr = ufrRepository.findByNom(ufrNom).orElse(null);
		if (ufr != null && !departementRepository.existsByNomAndUfr(nom, ufr)) {
			Departement departement = new Departement(nom, ufr);
			departementRepository.save(departement);
		}
	}

	private void insertFiliere(String nom, String departementNom) {
		Departement departement = departementRepository.findByNom(departementNom).orElse(null);
		if (departement != null && !filiereRepository.existsByNomAndDepartement(nom, departement)) {
			Filiere filiere = new Filiere(nom, departement);
			filiereRepository.save(filiere);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// Vérification si l'utilisateur existe avant d'ajouter
		insertUtilisateur("idiop@uasz.sn", "Ibrahima", "DIOP", "Passer123", "Responsable");
		insertUtilisateur("cmalack@uasz.sn", "Camin", "MALACK", "Passer123", "Stager");

		// Insérer les départements et les filières ici...
	}

	private void insertUtilisateur(String username, String prenom, String nom, String password, String role) {
		if (!utilisateurRepository.existsByUsername(username)) {
			String encodedPassword = passwordEncoder.encode(password);
			Utilisateur utilisateur = new Utilisateur();
			utilisateur.setUsername(username);
			utilisateur.setPrenom(prenom);
			utilisateur.setNom(nom);
			utilisateur.setPassword(encodedPassword);
			utilisateur.setDateCreation(new Date());
			utilisateurService.ajouter_Utilisateur(utilisateur);

			Role utilisateurRole = utilisateurService.ajouter_role(new Role(role));
			utilisateurService.ajouter_UtilisateurRoles(utilisateur, utilisateurRole);
		}
	}
}
