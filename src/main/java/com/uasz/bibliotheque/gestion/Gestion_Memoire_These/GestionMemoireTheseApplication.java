package com.uasz.bibliotheque.gestion.Gestion_Memoire_These;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class GestionMemoireTheseApplication implements CommandLineRunner {
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(GestionMemoireTheseApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception{
		/*Role Responsable = utilisateurService.ajouter_role(new Role("Responsable"));
		Role stager = utilisateurService.ajouter_role(new Role("Stager"));
		String password = passwordEncoder.encode("Passer123");

		Utilisateur user_1 = new Utilisateur();
		user_1.setNom("DIOP");
		user_1.setPrenom("Ibrahima");
		user_1.setUsername("idiop@uasz.sn");
		user_1.setPassword(password);
		user_1.setDateCreation(new Date());
		//user_1.setActive(true);

		utilisateurService.ajouter_Utilisateur(user_1);
		utilisateurService.ajouter_UtilisateurRoles(user_1, Responsable);

		Utilisateur user_2 = new Utilisateur();
		user_2.setNom("MALACK");
		user_2.setPrenom("Camin");
		user_2.setUsername("cmalack@uasz.sn");
		user_2.setPassword(password);
		user_2.setDateCreation(new Date());
		//user_2.setActive(true);
		//user_2.setLog("Ingénierie de Connaissances");
		utilisateurService.ajouter_Utilisateur(user_2);
		utilisateurService.ajouter_UtilisateurRoles(user_2, stager);*/

	}

}
