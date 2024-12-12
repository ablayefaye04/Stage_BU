package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import org.springframework.data.jpa.repository.JpaRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoireRepository extends JpaRepository<Memoire, Long>, JpaSpecificationExecutor<Memoire> {

    List<Memoire> findByType(TypeMemoire typeMemoire);
    @Query("SELECT DISTINCT m.filiere.nom FROM Memoire m")
    List<String> findDistinctFilieres();

    @Query("SELECT DISTINCT m.encadrant.nom FROM Memoire m")
    List<String> findDistinctEncadrants();
    long countByType(TypeMemoire type);

    @Query("SELECT m.annee, COUNT(m) FROM Memoire m GROUP BY m.annee ORDER BY m.annee ASC")
    List<Object[]> countMemosGroupedByYear();

    List<Memoire> findAllByType(TypeMemoire type);
    long countByTypeAndAnnee(TypeMemoire type, int annee);

    Optional<Memoire> findByTitre(String titre);

    @Query("SELECT m FROM Memoire m WHERE m.annee = :annee AND m.type = :type")
    List<Memoire> findByAnneeAndType(@Param("annee") int annee, @Param("type") TypeMemoire type);

}
