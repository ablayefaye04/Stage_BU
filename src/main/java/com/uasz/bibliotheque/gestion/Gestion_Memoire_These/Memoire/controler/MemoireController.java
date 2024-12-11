package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.MemoireRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.EncadrantService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.EtudiantService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.FiliereService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.MemoireService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MemoireController {

    private static final Logger logger = LoggerFactory.getLogger(MemoireController.class);

    @Autowired
    private MemoireService memoireService;
    @Autowired
    EtudiantService etudiantService;

    @Autowired
    EncadrantService encadrantService;
    @Autowired
    FiliereService filiereService;

    //formulaire d'ajout Licence
    @RequestMapping(value = "/ajoutMemoire", method = RequestMethod.GET)
    public String showAjoutMemoireForm(Model model) {
        model.addAttribute("memoire", new Memoire()); // ou l'objet correspondant
        return "ajoutMemoire";
    }

    //formulaire d'ajout Master
    @RequestMapping(value = "/ajoutMaster", method = RequestMethod.GET)
    public String afficherFormulaireMaster(Model model) {
        model.addAttribute("memoire", new Memoire()); // ou l'objet correspondant

        return "ajoutMaster"; // Assurez-vous que `formulaireMaster.html` est dans `templates`.
    }

    //formulaire d'ajout These
    @RequestMapping(value = "/ajoutThese", method = RequestMethod.GET)
    public String formulaireLicence(Model model) {
        model.addAttribute("memoire", new Memoire()); // ou l'objet correspondant

        return "ajoutThese"; // Nom du fichier Thymeleaf
    }


    @RequestMapping(value = "/memoires/ajouter", method = RequestMethod.POST)
    public String ajouterMemoire(
            @RequestParam("ufrNom") String ufrNom,
            @RequestParam("departementNom") String departementNom,
            @RequestParam("filiereNom") String filiereNom,
            @RequestParam("type") String type,
            @RequestParam("titre") String titre,
            @RequestParam("annee") int annee,
            @RequestParam("exemplaires") int exemplaires,
            @RequestParam("etudiantNom") String etudiantNom,
            @RequestParam("etudiantPrenom") String etudiantPrenom,
            @RequestParam("encadrantNom") String encadrantNom,
            @RequestParam("encadrantPrenom") String encadrantPrenom,
            Model model) {

        // Vérification de la présence de tous les paramètres
        if (ufrNom == null || ufrNom.isEmpty()) {
            logger.error("Le nom de l'UFR est manquant");
            model.addAttribute("error", "Le nom de l'UFR est manquant");
            return "ajoutMemoire";
        }

        if (departementNom == null || departementNom.isEmpty()) {
            logger.error("Le nom du département est manquant");
            model.addAttribute("error", "Le nom du département est manquant");
            return "ajoutMemoire";
        }

        if (filiereNom == null || filiereNom.isEmpty()) {
            logger.error("Le nom de la filière est manquant");
            model.addAttribute("error", "Le nom de la filière est manquant");
            return "ajoutMemoire";
        }

        if (type == null || type.isEmpty()) {
            logger.error("Le type de mémoire est manquant");
            model.addAttribute("error", "Le type de mémoire est manquant");
            return "ajoutMemoire";
        }

        if (titre == null || titre.isEmpty()) {
            logger.error("Le titre du mémoire est manquant");
            model.addAttribute("error", "Le titre du mémoire est manquant");
            return "ajoutMemoire";
        }

        if (annee <= 0) {
            logger.error("L'année est invalide");
            model.addAttribute("error", "L'année est invalide");
            return "ajoutMemoire";
        }

        if (exemplaires <= 0) {
            logger.error("Le nombre d'exemplaires est invalide");
            model.addAttribute("error", "Le nombre d'exemplaires est invalide");
            return "ajoutMemoire";
        }

        if (etudiantNom == null || etudiantNom.isEmpty()) {
            logger.error("Le nom de l'étudiant est manquant");
            model.addAttribute("error", "Le nom de l'étudiant est manquant");
            return "ajoutMemoire";
        }

        if (etudiantPrenom == null || etudiantPrenom.isEmpty()) {
            logger.error("Le prénom de l'étudiant est manquant");
            model.addAttribute("error", "Le prénom de l'étudiant est manquant");
            return "ajoutMemoire";
        }

        if (encadrantNom == null || encadrantNom.isEmpty()) {
            logger.error("Le nom de l'encadrant est manquant");
            model.addAttribute("error", "Le nom de l'encadrant est manquant");
            return "ajoutMemoire";
        }

        if (encadrantPrenom == null || encadrantPrenom.isEmpty()) {
            logger.error("Le prénom de l'encadrant est manquant");
            model.addAttribute("error", "Le prénom de l'encadrant est manquant");
            return "ajoutMemoire";
        }

        // Log des valeurs pour vérifier la réception des paramètres
        logger.info("Ajout du mémoire : UFR={}, Département={}, Filière={}, Type={}, Titre={}, Année={}, Exemplaires={}, Etudiant={}, Encadrant={}",
                ufrNom, departementNom, filiereNom, type, titre, annee, exemplaires, etudiantNom, encadrantNom);

        try {
            // Appel du service pour ajouter le mémoire
            memoireService.ajouterMemoire(
                    ufrNom, departementNom, filiereNom, type, titre, annee, exemplaires,
                    etudiantNom, etudiantPrenom, encadrantNom, encadrantPrenom
            );

            // Ajouter un message de succès au modèle
            model.addAttribute("message", "Mémoire ajouté avec succès !");

            return "redirect:/memoires/liste";

        } catch (RuntimeException e) {
            // Gestion d'une erreur
            logger.error("Erreur lors de l'ajout du mémoire", e);
            model.addAttribute("error", "Erreur : " + e.getMessage());
            return "ajoutMemoire";
        }
    }

    public MemoireController(MemoireService memoireService) {
        this.memoireService = memoireService;
    }

    private void ajouterStatistiques(Model model) {
        try {
            logger.info("Récupération des statistiques des mémoires.");

            // Compter les mémoires par type
            long licenceCount = memoireService.countMemosByType(TypeMemoire.LICENCE);
            long masterCount = memoireService.countMemosByType(TypeMemoire.MASTER);
            long doctoratCount = memoireService.countMemosByType(TypeMemoire.DOCTORAT);

            model.addAttribute("licenceCount", licenceCount);
            model.addAttribute("masterCount", masterCount);
            model.addAttribute("doctoratCount", doctoratCount);

            // Préparation des données pour le graphique
            List<Long> stats = List.of(licenceCount, masterCount, doctoratCount);
            model.addAttribute("stats", stats);

            // Récupération des statistiques par année et par type
            Map<Integer, Long> licencesParAnnee = memoireService.countMemosByTypeAndYear(TypeMemoire.LICENCE);
            Map<Integer, Long> mastersParAnnee = memoireService.countMemosByTypeAndYear(TypeMemoire.MASTER);
            Map<Integer, Long> doctoratsParAnnee = memoireService.countMemosByTypeAndYear(TypeMemoire.DOCTORAT);

            // Regrouper les années
            Set<Integer> anneesSet = new TreeSet<>();
            anneesSet.addAll(licencesParAnnee.keySet());
            anneesSet.addAll(mastersParAnnee.keySet());
            anneesSet.addAll(doctoratsParAnnee.keySet());
            List<Integer> annees = new ArrayList<>(anneesSet);

            // Préparer les données pour les graphiques
            List<Long> licenceCounts = annees.stream().map(year -> licencesParAnnee.getOrDefault(year, 0L)).collect(Collectors.toList());
            List<Long> masterCounts = annees.stream().map(year -> mastersParAnnee.getOrDefault(year, 0L)).collect(Collectors.toList());
            List<Long> doctoratCounts = annees.stream().map(year -> doctoratsParAnnee.getOrDefault(year, 0L)).collect(Collectors.toList());

            model.addAttribute("annees", annees);
            model.addAttribute("licenceCounts", licenceCounts);
            model.addAttribute("masterCounts", masterCounts);
            model.addAttribute("doctoratCounts", doctoratCounts);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des statistiques : ", e);
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la récupération des statistiques.");
        }
    }

    //liste de tous les memoires
    @RequestMapping(value = "/memoires/liste", method = RequestMethod.GET)
    public String afficherMemoires(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) Integer annee,
            Model model, Principal principal
    ) {

        // Gestion de l'utilisateur connecté
        if (principal != null) {
            Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null) {
                // Ajouter les informations de l'utilisateur au modèle
                model.addAttribute("nom", utilisateur.getNom());
                model.addAttribute("prenom", utilisateur.getPrenom());

                // Extraire les rôles et les ajouter
                String roles = utilisateur.getRoles().stream()
                        .map(Role::getRole)
                        .reduce((role1, role2) -> role1 + ", " + role2)
                        .orElse("Aucun rôle");
                model.addAttribute("roles", roles);
            }
        }

        // Définir une spécification de base
        Specification<Memoire> spec = Specification.where(null);

        // Ajouter des conditions de recherche si des paramètres sont fournis
        boolean hasSearchParams = false; // Variable pour savoir si une recherche a été effectuée

        // Si un champ est rempli, ajouter une condition à la recherche
        if (cote != null && !cote.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(cote));
            hasSearchParams = true;
            System.out.println("Recherche par Cote : " + cote);  // Débogage
        }
        if (titre != null && !titre.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(titre));
            hasSearchParams = true;
            System.out.println("Recherche par Titre : " + titre);  // Débogage
        }
        if (filiere != null && !filiere.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(filiere));
            hasSearchParams = true;
            System.out.println("Recherche par filiere : " + filiere);  // Débogage
        }
        if (etudiant != null && !etudiant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(etudiant));
            hasSearchParams = true;
            System.out.println("Recherche par Etudiant : " + etudiant);  // Débogage
        }
        if (encadrant != null && !encadrant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(encadrant));
            hasSearchParams = true;
            System.out.println("Recherche par Encadrant : " + encadrant);  // Débogage
        }
        if (annee != null) {
            spec = spec.and(MemoireSpecifications.withAnnee(annee));
            hasSearchParams = true;
            System.out.println("Recherche par Année : " + annee);  // Débogage
        }

        // Exécuter la recherche si des paramètres de recherche sont présents
        if (hasSearchParams) {
            List<Memoire> memoires = memoireService.searchMemos(spec);
            model.addAttribute("memoires", memoires);

            if (memoires.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
            }
        } else {
            // Si aucun critère n'est fourni, récupérer tous les mémoires groupés
            Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresGroupes();
            model.addAttribute("memoiresGroupes", memoiresGroupes);
        }

        ajouterStatistiques(model); // Appel de la méthode pour ajouter les statistiques

        return "essaie"; // Vue pour afficher les résultats de recherche
    }

    /**
     * Affiche la page de modification avec ou sans recherche.
     */
    @RequestMapping(value = "/modifier", method = RequestMethod.GET)
    public String afficherFormulaireRechercheModification(Model model) {
        model.addAttribute("memoire", new Memoire()); // Mémoire vide pour le formulaire
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("encadrants", encadrantService.findAll());
        model.addAttribute("filieres", filiereService.findAll());
        return "modifierMemoire";
    }
    
    /**
     * Affiche le formulaire pré-rempli pour un mémoire donné.
     */
    @GetMapping("/memoires/modifier/{id}")
    public String afficherFormulaireModification(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Memoire memoire = memoireService.getMemoireById(id);
        if (memoire == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mémoire introuvable !");
            return "redirect:/memoires/liste";
        }
        List<Filiere> filieres = filiereService.findAll(); // Récupération des filières
        List<Etudiant> etudiants = etudiantService.findAll();
        List<Encadrant> encadrants = encadrantService.findAll();

        model.addAttribute("memoire", memoire);
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("encadrants", encadrantService.findAll());
        model.addAttribute("filieres", filiereService.findAll());
        return "modifierMemoire";
    }

    /**
     * Recherche des mémoires en fonction des critères fournis.
     */ //Pour la modification
    @GetMapping("/memoires/modifier/recherche")
    public String rechercherMemosModifier(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) Integer annee,
            Model model
    ) {
        // Rassembler les paramètres de recherche dans un Map
        Map<String, String> params = new HashMap<>();
        if (cote != null) params.put("cote", cote);
        if (titre != null) params.put("titre", titre);
        if (filiere != null) params.put("filiere", filiere);
        if (etudiant != null) params.put("etudiant", etudiant);
        if (encadrant != null) params.put("encadrant", encadrant);
        if (annee != null) params.put("annee", annee.toString());

        // Appel de la méthode de recherche
        List<Memoire> memoires = memoireService.rechercherMemos(params);

        // Si aucun mémoire n'est trouvé, afficher un message d'erreur
        if (memoires.isEmpty()) {
            model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
        } else if (memoires.size() == 1) {
            // Si un seul mémoire est trouvé, pré-remplir les champs de modification
            model.addAttribute("memoire", memoires.get(0));
        } else {
            // Si plusieurs mémoires sont trouvés, afficher la liste pour que l'utilisateur puisse en choisir un
            model.addAttribute("memoires", memoires);
            model.addAttribute("message", "Plusieurs mémoires trouvés, veuillez en sélectionner un.");
        }

        // Ajouter les autres attributs nécessaires pour la modification (comme les listes d'étudiants, encadrants, etc.)
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("encadrants", encadrantService.findAll());
        model.addAttribute("filieres", filiereService.findAll());

        return "modifierMemoire";
    }

    //Suppression
    @GetMapping("/memoires/supprimer/{id}")
    public String supprimerMemoire(@PathVariable Long id) {
        memoireService.deleteMemoire(id);
        return "redirect:/memoires/liste"; // Redirection après suppression
    }

    @PostMapping("/memoires/modifier")
    public String modifierMemoire(@ModelAttribute Memoire memoire, RedirectAttributes redirectAttributes) {
        try {
            // Vérifie si le mémoire à modifier existe
            Memoire memoireExistant = memoireService.getMemoireById(memoire.getId());
            if (memoireExistant == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mémoire introuvable !");
                return "redirect:/memoires/liste";
            }

            // Récupère l'étudiant et l'encadrant à partir de leurs ID respectifs
            if (memoire.getEtudiant() != null && memoire.getEtudiant().getId() != null) {
                Etudiant etudiant = etudiantService.findById(memoire.getEtudiant().getId());
                if (etudiant != null) {
                    memoire.setEtudiant(etudiant); // Met à jour l'étudiant
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Étudiant introuvable !");
                    return "redirect:/memoires/liste";
                }
            }

            if (memoire.getEncadrant() != null && memoire.getEncadrant().getId() != null) {
                Encadrant encadrant = encadrantService.findById(memoire.getEncadrant().getId());
                if (encadrant != null) {
                    memoire.setEncadrant(encadrant); // Met à jour l'encadrant
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "Encadrant introuvable !");
                    return "redirect:/memoires/liste";
                }
            }

            // Effectue la modification du mémoire
            memoireService.modifierMemoire(memoire.getId(), memoire);
            redirectAttributes.addFlashAttribute("successMessage", "Mémoire mis à jour avec succès !");
            return "redirect:/memoires/liste";

        } catch (Exception e) {
            // Capture les erreurs inattendues
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de la mise à jour du mémoire.");
            return "redirect:/memoires/liste";
        }
    }

}
