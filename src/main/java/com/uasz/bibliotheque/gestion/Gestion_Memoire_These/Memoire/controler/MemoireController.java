package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.service.MessageService;
import org.apache.poi.ss.formula.functions.T;
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
    private MessageService messageService;

    @Autowired
    private TheseService theseService;
    @Autowired
    private NotificationService notificationService ;
    @Autowired
    EtudiantService etudiantService;

    @Autowired
    EncadrantService encadrantService;
    @Autowired
    FiliereService filiereService;
    @Autowired
    private UfrService ufrService;

    @Autowired
    private DepartementService departementService;
    private final StatistiquesService statistiquesService;



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

    public MemoireController(MemoireService memoireService, StatistiquesService statistiquesService) {
        this.memoireService = memoireService;
        this.statistiquesService = statistiquesService;
    }


    //liste de tous les memoires
    @RequestMapping(value = "/memoires/liste", method = RequestMethod.GET)
    public String afficherMemoires(Model model, Principal principal) {
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

        // Si aucun critère n'est fourni, récupérer tous les mémoires groupés
        Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresGroupes();
        model.addAttribute("memoiresGroupes", memoiresGroupes);
        model.addAttribute("notifications", notificationService.getNotificationNonLue());
        model.addAttribute("messages", messageService.getMessages());
        model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel

        statistiquesService.ajouterStatistiques(model);

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

  /*  @GetMapping("/rechercheParAnnee")
    public String rechercheParAnnee(@RequestParam("annee") int annee,
                                    @RequestParam("type") TypeMemoire type,
                                    Model model) {
        try {
           // TypeMemoire typeEnum = TypeMemoire.valueOf(type.toUpperCase());
            List<Memoire> resultats = memoireService.findByAnneeAndType(annee, String.valueOf(type));
            model.addAttribute("resultats", resultats);
            model.addAttribute("anneeRecherchee", annee);
            model.addAttribute("typeRecherche", type);
            if (resultats.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour l'année " + annee + " et le type " + type);
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", "Type de mémoire invalide : " + type);
        } catch (Exception e) {
            model.addAttribute("erreur", "Une erreur est survenue : " + e.getMessage());
        }
        return "resultatsRecherche";
    }*/

    @GetMapping("/rechercheParAnnee")
    public String rechercheParAnnee(@RequestParam("annee") int annee,
                                    @RequestParam("type") String type,
                                    Model model) {
        try {
            TypeMemoire typeEnum = TypeMemoire.valueOf(type.toUpperCase());
            List<Memoire> resultats = memoireService.findByAnneeAndType(annee, String.valueOf(typeEnum));
            model.addAttribute("resultats", resultats);
            model.addAttribute("anneeRecherchee", annee);
            model.addAttribute("typeRecherche", typeEnum);
            if (resultats.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour l'année " + annee + " et le type " + typeEnum);
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", "Type de mémoire invalide : " + type);
        } catch (Exception e) {
            model.addAttribute("erreur", "Une erreur est survenue : " + e.getMessage());
        }
        return "resultatsRecherche";
    }



    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String voir(Model model) {
        return "index"; // Assurez-vous que `formulaireMaster.html` est dans `templates`.
    }

    /**
     * Affiche le formulaire de filtrage des mémoires de Licence.
     */
    @GetMapping("/licence")
    public String afficherToutesLesMemoires(Model model) {
        // Récupérer toutes les mémoires de type LICENCE
        List<Memoire> memoires = memoireService.getAllMemoiresLicence();

        // Ajouter les mémoires et les UFR au modèle
        model.addAttribute("memoires", memoires);
        return "licence";
    }

    @GetMapping("/master")
    public String afficherToutesLesMemoiresMasters(Model model) {
        // Récupérer toutes les mémoires de type Master
        List<Memoire> memoires = memoireService.getAllMemoiresMaster();

        // Ajouter les mémoires et les UFR au modèle
        model.addAttribute("memoires", memoires);
        return "master";
    }

    @GetMapping("/doctorat")
    public String afficherToutesLesMemoiresTheses(Model model) {
        // Récupérer toutes les mémoires de type These
        List<These> memoires = theseService.getAllThese();

        // Ajouter les mémoires au modèle
        model.addAttribute("memoires", memoires);
        return "doctorat"; // Assure-toi que "doctorat" est le nom du fichier Thymeleaf
    }

    /**
     * Filtre et affiche uniquement les mémoires de Licence.
     */
    @PostMapping("/filtre/licence")
    public String filtrerMemoires(
            @RequestParam String ufrNom,
            @RequestParam String departementNom,
            @RequestParam String filiereNom,
            Model model) {

        // Récupérer uniquement les mémoires de type LICENCE
        List<Memoire> memoires = memoireService.getMemoiresLicenceFiltres(ufrNom, departementNom, filiereNom);

        // Grouper les mémoires par UFR > Département
        Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoires.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getFiliere().getDepartement().getUfr().getNom(),
                        Collectors.groupingBy(m -> m.getFiliere().getDepartement().getNom())
                ));

        // Ajouter les données au modèle
        model.addAttribute("ufrs", ufrService.findAllUfrs());
        model.addAttribute("memoiresGroupes", memoiresGroupes);
        model.addAttribute("selection", Map.of(
                "ufr", ufrNom,
                "departement", departementNom,
                "filiere", filiereNom
        ));
        model.addAttribute("rechercheEffectuees", true); // Ajoute un indicateur de recherche

        return "licence";
    }

    /**
     * Filtre et affiche uniquement les mémoires de Master.
     */
    @PostMapping("/filtre/master")
    public String filtrerMemoiresMaster(
            @RequestParam String ufrNom,
            @RequestParam String departementNom,
            @RequestParam String filiereNom,
            Model model) {

        // Récupérer uniquement les mémoires de type Master
        List<Memoire> memoires = memoireService.getMemoiresMastersFiltres(ufrNom, departementNom, filiereNom);

        // Grouper les mémoires par UFR > Département
        Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoires.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getFiliere().getDepartement().getUfr().getNom(),
                        Collectors.groupingBy(m -> m.getFiliere().getDepartement().getNom())
                ));

        // Ajouter les données au modèle
        model.addAttribute("ufrs", ufrService.findAllUfrs());
        model.addAttribute("memoiresGroupes", memoiresGroupes);
        model.addAttribute("selection", Map.of(
                "ufr", ufrNom,
                "departement", departementNom,
                "filiere", filiereNom
        ));
        model.addAttribute("rechercheEffectuees", true); // Ajoute un indicateur de recherche

        return "master";
    }

    @PostMapping("/filtre/these")
    public String filtrerThese(
            @RequestParam String ufrNom,
            @RequestParam String ecoleDoctoraleNom,
            Model model) {

        // Récupérer uniquement les mémoires de type Doctorat
        List<These> memoires = theseService.getMemoiresTheseFiltres(ufrNom, ecoleDoctoraleNom);

        // Grouper les mémoires par UFR > Département > Filière
        Map<String, Map<String, List<These>>> memoiresGroupes = memoires.stream()
                .collect(Collectors.groupingBy(
                        m -> m.getEcoleDoctorat().getUfr().getNom(), // Accès à UFR via EcoleDoctorat
                        Collectors.groupingBy(
                                m -> m.getEcoleDoctorat().getUfr().getNom() // Accès au Département
                        )
                ));

        // Ajouter les données au modèle
        model.addAttribute("ufrs", ufrService.findAllUfrs()); // Liste des UFRs
        model.addAttribute("memoiresGroupes", memoiresGroupes); // Groupes de mémoires
        model.addAttribute("selection", Map.of(
                "ufr", ufrNom,
                "ecoleDoctorale", ecoleDoctoraleNom
        ));
        model.addAttribute("rechercheEffectuees", true); // Indicateur que la recherche a été effectuée

        return "doctorat"; // Vue à retourner
    }
}