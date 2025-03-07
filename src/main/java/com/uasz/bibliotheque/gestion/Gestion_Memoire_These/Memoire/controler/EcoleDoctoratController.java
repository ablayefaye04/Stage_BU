package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Encadrant;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Etudiant;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.TheseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EcoleDoctoratController {

    @Autowired
    private TheseRepository theseRepository;

    @Autowired
    private TheseService theseService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EncadrantRepository encadrantRepository;

    @Autowired
    private EcoleDoctoraleRepository ecoleDoctoraleRepository;
    private String extractShortName(String fullName) {
        int start = fullName.indexOf("(");
        int end = fullName.indexOf(")");

        if (start != -1 && end != -1 && start < end) {
            return fullName.substring(start + 1, end); // Récupère uniquement le texte entre parenthèses
        }

        return fullName; // Si pas de parenthèses, retourne le nom complet
    }


    private String generateCote(String nomEcole, int annee, int exemplaires) {
        // Récupérer les deux derniers chiffres de l'année (2025 → 25)
        String anneeShort = String.valueOf(annee).substring(2);

        // Générer la cote au format "ED-STI/25/1"
        return nomEcole + "/" + anneeShort + "/" + exemplaires;
    }


    @PostMapping("/theses/ajouter")
    public String addThesis(@RequestParam String titre,
                            @RequestParam String etudiantNom,
                            @RequestParam String etudiantPrenom,
                            @RequestParam String encadrantNom,
                            @RequestParam String encadrantPrenom,
                            @RequestParam int annee,
                            @RequestParam int exemplaires,
                            @RequestParam String ecoleDoctorale,
                            Model model) {
        try {
            // Vérification des champs vides
            if (titre.isBlank() || etudiantNom.isBlank() || etudiantPrenom.isBlank()
                    || encadrantNom.isBlank() || encadrantPrenom.isBlank()) {
                model.addAttribute("error", "Tous les champs doivent être remplis !");
                return "ajouterThese";
            }

            // Gestion de l'étudiant
            Etudiant etudiant = etudiantRepository.findByNomAndPrenom(etudiantNom, etudiantPrenom)
                    .orElseGet(() -> etudiantRepository.save(new Etudiant(null, etudiantNom, etudiantPrenom)));

            // Gestion de l'encadrant
            Encadrant encadrant = encadrantRepository.findByNomAndPrenom(encadrantNom, encadrantPrenom)
                    .orElseGet(() -> {
                        Encadrant newEncadrant = new Encadrant();
                        newEncadrant.setNom(encadrantNom);
                        newEncadrant.setPrenom(encadrantPrenom);
                        return encadrantRepository.save(newEncadrant);
                    });

            // Vérification de l'école doctorale
            Optional<EcoleDoctorat> ecoleDoctoraleOpt = ecoleDoctoraleRepository.findByNom(ecoleDoctorale);
            if (ecoleDoctoraleOpt.isEmpty()) {
                model.addAttribute("error", "L'école doctorale sélectionnée est invalide.");
                return "ajouterThese";
            }

            // Récupération de l'école doctorale
            EcoleDoctorat ecoleDoctoratEntity = ecoleDoctoraleOpt.get();

            // Extraire uniquement l'abréviation de l'école doctorale
            String shortName = extractShortName(ecoleDoctoratEntity.getNom());

            // Générer la cote en utilisant l'abréviation
            String coteGeneree = generateCote(shortName, annee, exemplaires);

            // Création et sauvegarde de la thèse
            These these = new These();
            these.setCote(coteGeneree);  // Cote générée automatiquement
            these.setTitre(titre);
            these.setEtudiant(etudiant);
            these.setEncadrant(encadrant);
            these.setAnnee(annee);
            these.setExemplaires(exemplaires);
            these.setEcoleDoctorat(ecoleDoctoratEntity);

            theseRepository.save(these);

            return "redirect:/memoires/doctorats";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout de la thèse : " + e.getMessage());
            return "ajouterThese";
        }
    }

    //listess des these
    @GetMapping("/memoires/doctorats")
    public String listTheses(Model model) {
        List<These> theses = theseRepository.findAll();

        // Grouper les thèses par UFR
        Map<String, List<These>> thesesParUFR = theses.stream()
                .filter(these -> these.getEcoleDoctorat() != null && these.getEcoleDoctorat().getUfr() != null)
                .collect(Collectors.groupingBy(these -> these.getEcoleDoctorat().getUfr().getNom()));

        // Créer un mappage de chaque école doctorale pour chaque UFR
        Map<String, String> ecoleDoctoraleData = new HashMap<>();
        ecoleDoctoraleData.put("UFR Sciences et Techniques (ST)", "Ecole Doctorat Sciences, Technologies et Ingénierie (ED-STI)");
        ecoleDoctoraleData.put("UFR Lettres, Art, Sciences Humaines (LASHU)", "Ecole Doctorat Espaces, Sociétés et Humanités (ED-ESH)");

        model.addAttribute("thesesParUFR", thesesParUFR);
        model.addAttribute("ecoleDoctoraleData", ecoleDoctoraleData);
        for (Map.Entry<String, List<These>> entry : thesesParUFR.entrySet()) {
            System.out.println("UFR: " + entry.getKey() + " - Thèses: " + entry.getValue().size());
        }

        return "doctorat"; // nom de la vue qui affiche la liste des thèses
    }

    // Méthode pour afficher la page de modification de la thèse
    @GetMapping("/modifier/{id}")
    public String editThesis(@PathVariable("id") Long thesisId, Model model) {
        These these = theseService.getThesisById(thesisId);
        if (these == null) {
            return "redirect:/theses"; // Redirige si la thèse n'existe pas
        }
        model.addAttribute("these", these);
        return "theses/edit"; // Affiche la page de modification avec les informations de la thèse
    }

    // Méthode pour traiter la modification d'une thèse
    @PostMapping("/modifier/{id}")
    public String updateThesis(@PathVariable("id") Long thesisId, These updatedThesis) {
        theseService.updateThesis(thesisId, updatedThesis);
        return "redirect:/theses"; // Redirige vers la liste des thèses après la modification
    }

    // Méthode pour supprimer une thèse
    @GetMapping("/supprimer/{id}")
    public String deleteThesis(@PathVariable("id") Long thesisId) {
        theseService.deleteThesis(thesisId);
        return "redirect:/theses"; // Redirige vers la liste des thèses après la suppression
    }

}
