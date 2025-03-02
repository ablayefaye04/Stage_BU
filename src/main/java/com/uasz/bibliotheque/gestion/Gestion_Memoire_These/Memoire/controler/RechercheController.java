package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.MemoireService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller // Utilisation correcte de l'annotation pour Thymeleaf
public class RechercheController {

    private final MemoireService memoireService;

    @Autowired
    public RechercheController(MemoireService memoireService) {
        this.memoireService = memoireService;
    }

    // Afficher les mémoires de Licence
    @RequestMapping(value = "/memoires/licences", method = RequestMethod.GET)
    public String afficherMemoiresLicence(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) Integer annee,
            Model model
    ) {
        // Définir une spécification de base pour les mémoires de type Licence
        Specification<Memoire> spec = Specification.where(MemoireSpecifications.withType(TypeMemoire.LICENCE));

        // Vérifier si des critères de recherche ont été fournis
        boolean hasSearchParams = false;

        if (cote != null && !cote.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(cote));
            hasSearchParams = true;
        }
        if (titre != null && !titre.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(titre));
            hasSearchParams = true;
        }
        if (etudiant != null && !etudiant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(etudiant));
            hasSearchParams = true;
        }
        if (encadrant != null && !encadrant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(encadrant));
            hasSearchParams = true;
        }
        if (filiere != null && !filiere.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(filiere));
            hasSearchParams = true;
        }
        if (annee != null) {
            spec = spec.and(MemoireSpecifications.withAnnee(annee));
            hasSearchParams = true;
        }

        if (hasSearchParams) {
            // Effectuer une recherche selon les critères
            List<Memoire> memoiresTrouves = memoireService.searchMemos(spec);
            model.addAttribute("memoires", memoiresTrouves);
            model.addAttribute("nombreMemoiresTrouves", memoiresTrouves.size());

            if (memoiresTrouves.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
            }
        } else {
            // Si aucune recherche n'est effectuée, récupérer les mémoires groupés
            Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresLicenceGroupes();
            model.addAttribute("memoiresGroupes", memoiresGroupes);

            // Calculer le nombre total de mémoires
            int nombreTotalMemoires = memoiresGroupes.values().stream()
                    .flatMap(map -> map.values().stream())
                    .mapToInt(List::size)
                    .sum();
            model.addAttribute("nombreMemoires", nombreTotalMemoires);
        }

        model.addAttribute("typeMemoire", "Licence");
        return "licence"; // Vue dédiée
    }


    // Afficher les mémoires de Master
    @RequestMapping(value = "/memoires/masters", method = RequestMethod.GET)
    public String afficherMemoiresMasters(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) Integer annee,
            Model model
    ) {
        // Définir une spécification de base pour les mémoires de type Licence
        Specification<Memoire> spec = Specification.where(MemoireSpecifications.withType(TypeMemoire.MASTER));

        // Vérifier si des critères de recherche ont été fournis
        boolean hasSearchParams = false;

        if (cote != null && !cote.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(cote));
            hasSearchParams = true;
        }
        if (titre != null && !titre.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(titre));
            hasSearchParams = true;
        }
        if (etudiant != null && !etudiant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(etudiant));
            hasSearchParams = true;
        }
        if (encadrant != null && !encadrant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(encadrant));
            hasSearchParams = true;
        }
        if (filiere != null && !filiere.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(filiere));
            hasSearchParams = true;
        }
        if (annee != null) {
            spec = spec.and(MemoireSpecifications.withAnnee(annee));
            hasSearchParams = true;
        }

        if (hasSearchParams) {
            // Effectuer une recherche selon les critères
            List<Memoire> memoiresTrouves = memoireService.searchMemos(spec);
            model.addAttribute("memoires", memoiresTrouves);
            model.addAttribute("nombreMemoiresTrouves", memoiresTrouves.size());

            if (memoiresTrouves.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
            }
        } else {
            // Si aucune recherche n'est effectuée, récupérer les mémoires groupés
            Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresMasterGroupes();
            model.addAttribute("memoiresGroupes", memoiresGroupes);

            // Calculer le nombre total de mémoires
            int nombreTotalMemoires = memoiresGroupes.values().stream()
                    .flatMap(map -> map.values().stream())
                    .mapToInt(List::size)
                    .sum();
            model.addAttribute("nombreMemoires", nombreTotalMemoires);
        }

        model.addAttribute("typeMemoire", "Master");
        return "master"; // Vue dédiée
    }

    // Afficher les mémoires de Doctorat
    @RequestMapping(value = "/theses", method = RequestMethod.GET)
    public String afficherMemoiresDoctorats(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) Integer annee,
            Model model
    ) {
        // Définir une spécification de base pour les mémoires de type Licence
        Specification<Memoire> spec = Specification.where(MemoireSpecifications.withType(TypeMemoire.DOCTORAT));

        // Vérifier si des critères de recherche ont été fournis
        boolean hasSearchParams = false;

        if (cote != null && !cote.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(cote));
            hasSearchParams = true;
        }
        if (titre != null && !titre.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(titre));
            hasSearchParams = true;
        }
        if (etudiant != null && !etudiant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(etudiant));
            hasSearchParams = true;
        }
        if (encadrant != null && !encadrant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(encadrant));
            hasSearchParams = true;
        }
        if (filiere != null && !filiere.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(filiere));
            hasSearchParams = true;
        }
        if (annee != null) {
            spec = spec.and(MemoireSpecifications.withAnnee(annee));
            hasSearchParams = true;
        }

        if (hasSearchParams) {
            // Effectuer une recherche selon les critères
            List<Memoire> memoiresTrouves = memoireService.searchMemos(spec);
            model.addAttribute("memoires", memoiresTrouves);
            model.addAttribute("nombreMemoiresTrouves", memoiresTrouves.size());

            if (memoiresTrouves.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
            }
        } else {
            // Si aucune recherche n'est effectuée, récupérer les mémoires groupés
            Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresDoctoratGroupes();
            model.addAttribute("memoiresGroupes", memoiresGroupes);

            // Calculer le nombre total de mémoires
            int nombreTotalMemoires = memoiresGroupes.values().stream()
                    .flatMap(map -> map.values().stream())
                    .mapToInt(List::size)
                    .sum();
            model.addAttribute("nombreMemoires", nombreTotalMemoires);
        }

        model.addAttribute("typeMemoire", "Doctorat");
        return "doctorat"; // Vue dédiée
    }

}
