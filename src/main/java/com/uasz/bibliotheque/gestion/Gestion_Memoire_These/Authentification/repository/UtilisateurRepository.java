package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u WHERE u.username = :username")
    Utilisateur findUtilisateurByUsername(@Param("username") String username);



}
