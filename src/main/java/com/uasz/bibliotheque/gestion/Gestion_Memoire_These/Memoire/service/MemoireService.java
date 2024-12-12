package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemoireService {
    @Autowired
    private MemoireRepository memoireRepository;

    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private UfrRepository ufrRepository;
    @Autowired
    public UserRepository userRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EncadrantRepository encadrantRepository;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EncadrantService encadrantService;

    @Autowired
    private FiliereService filiereService;


    // Récupère toutes les filières
    public List<Filiere> getAllFilieres() {
        return filiereRepository.findAll();
    }

    // Méthode pour récupérer tous les mémoires
    public List<Memoire> getAllMemoires() {
        return memoireRepository.findAll();
    }

    // Méthode pour récupérer un mémoire par son ID
    public Memoire getMemoireById(Long id) {
        Optional<Memoire> memoire = memoireRepository.findById(id);
        return memoire.orElseThrow(() -> new RuntimeException("Mémoire non trouvé"));
    }

    // Méthode pour générer la cote basée sur la filière, l'année et le nombre d'exemplaires
    public String genererCote(TypeMemoire type, Filiere filiere, int annee, int exemplaires) {
        String prefixeType = "";
        switch (type) {
            case LICENCE -> prefixeType = "L";
            case MASTER -> prefixeType = "M";
            case DOCTORAT -> prefixeType = "D";
        }

        String initialesFiliere = convertirEnInitiales(filiere.getNom());
        return prefixeType + initialesFiliere + (annee % 100) + "/" + exemplaires;
    }

    // Méthode pour convertir la filière en initiales
    private String convertirEnInitiales(String filiere) {
        StringBuilder initiales = new StringBuilder();
        String[] mots = filiere.split("_");
        for (String mot : mots) {
            if (!mot.isEmpty()) {
                initiales.append(mot.charAt(0));
            }
        }
        return initiales.toString().toUpperCase();
    }

    // Ajouter une mémoire
    public void ajouterMemoire(String ufrNom, String departementNom, String filiereNom, String type, String titre, int annee,
                               int exemplaires, String etudiantNom,String etudiantPrenom, String encadrantPrenom, String encadrantNom) {
        // Obtenez la filière
        Filiere filiere = filiereRepository.findByNom(filiereNom)
                .orElseThrow(() -> new RuntimeException("Filière introuvable"));

        // Récupérer le département et l'UFR à partir de la base de données
        Departement departement = departementRepository.findByNom(departementNom)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));

        Ufr ufr = ufrRepository.findByNom(ufrNom)
                .orElseThrow(() -> new RuntimeException("UFR introuvable"));

        // Vérification que l'UFR et le département sont cohérents avec la filière (optionnel)
        if (!departement.getUfr().equals(ufr)) {
            throw new RuntimeException("L'UFR ne correspond pas à celui du département pour cette filière");
        }

        // Gestion de l'étudiant
        Etudiant etudiant = etudiantRepository.findByNomAndPrenom(etudiantNom, etudiantPrenom)
                .orElseGet(() -> etudiantRepository.save(new Etudiant(null, etudiantNom, etudiantPrenom)));

        // Gestion de l'encadrant
        Encadrant encadrant = encadrantRepository.findByNomAndPrenomAndFiliere(encadrantNom, encadrantPrenom, filiere)
                .orElseGet(() -> encadrantRepository.save(new Encadrant(null, encadrantNom, encadrantPrenom, filiere)));

        // Créez un objet Mémoire et configurez ses valeurs
        Memoire memoire = new Memoire();
        memoire.setTitre(titre);
        memoire.setAnnee(annee);
        memoire.setExemplaires(exemplaires);
        memoire.setEtudiant(etudiant);
        memoire.setEncadrant(encadrant);
        memoire.setType(TypeMemoire.valueOf(type.toUpperCase()));
        memoire.setFiliere(filiere);
        memoire.setDepartement(departement);  // Remplir le département
        memoire.setUfr(ufr);  // Remplir l'UFR

        // Générez la cote (si nécessaire)
        String cote = genererCote(memoire.getType(), memoire.getFiliere(), annee, exemplaires);
        memoire.setCote(cote);

        memoireRepository.save(memoire);
    }

    // Méthode pour modifier un mémoire existant
    public Memoire modifierMemoire(Long id, Memoire memoireModifiee) {
        // Vérifie si le mémoire existe
        Memoire memoireExistante = getMemoireById(id);
        if (memoireExistante == null) {
            throw new IllegalArgumentException("Mémoire avec ID " + id + " n'existe pas.");
        }

        // Gestion de l'étudiant
        Etudiant etudiant = null;
        if (memoireModifiee.getEtudiant() != null) {
            if (memoireModifiee.getEtudiant().getId() != null) {
                // Si l'ID de l'étudiant est fourni, récupérer l'étudiant existant
                etudiant = etudiantService.findById(memoireModifiee.getEtudiant().getId());
            } else {
                // Sinon, créer un nouvel étudiant avec les informations fournies
                etudiant = etudiantService.createNewStudent(memoireModifiee.getEtudiant());
            }
        }

        // Gestion de l'encadrant
        Encadrant encadrant = null;
        if (memoireModifiee.getEncadrant() != null) {
            if (memoireModifiee.getEncadrant().getId() != null) {
                // Si l'ID de l'encadrant est fourni, récupérer l'encadrant existant
                encadrant = encadrantService.findById(memoireModifiee.getEncadrant().getId());
            } else {
                // Sinon, créer un nouvel encadrant avec les informations fournies
                encadrant = encadrantService.createNewSupervisor(memoireModifiee.getEncadrant());
            }
        }

        // Gestion de la filière
        Filiere filiere = null;
        if (memoireModifiee.getFiliere() != null && memoireModifiee.getFiliere().getNom() != null) {
            filiere = filiereService.findByNom(memoireModifiee.getFiliere().getNom())
                    .orElseGet(() -> filiereService.createNewField(memoireModifiee.getFiliere()));
        }

        // Mise à jour des champs du mémoire existant
        memoireExistante.setTitre(memoireModifiee.getTitre());
        memoireExistante.setEtudiant(etudiant);
        memoireExistante.setEncadrant(encadrant);
        memoireExistante.setFiliere(filiere);
        memoireExistante.setAnnee(memoireModifiee.getAnnee());
        memoireExistante.setExemplaires(memoireModifiee.getExemplaires());

        // Regénérer la cote avec les nouvelles informations
        TypeMemoire type = memoireExistante.getType(); // Assurez-vous que TypeMemoire est bien défini
        String nouvelleCote = genererCote(type, filiere, memoireModifiee.getAnnee(), memoireModifiee.getExemplaires());
        memoireExistante.setCote(nouvelleCote);  // Mise à jour de la cote

        // Sauvegarde et retour de l'entité mise à jour
        return memoireRepository.save(memoireExistante);
    }


    public List<Memoire> rechercherMemos(Map<String, String> params) {
        Specification<Memoire> spec = Specification.where(null);

        // Construire la spécification dynamiquement en fonction des paramètres
        if (params.containsKey("cote") && !params.get("cote").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(params.get("cote")));
        }
        if (params.containsKey("titre") && !params.get("titre").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(params.get("titre")));
        }
        if (params.containsKey("etudiant") && !params.get("etudiant").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(params.get("etudiant")));
        }
        if (params.containsKey("encadrant") && !params.get("encadrant").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(params.get("encadrant")));
        }
        if (params.containsKey("annee") && !params.get("annee").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withAnnee(Integer.parseInt(params.get("annee"))));
        }
        if (params.containsKey("filiere") && !params.get("filiere").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(params.get("filiere")));
        }
        // Ajouter un filtre pour le type de mémoire (Licence, Master, Doctorat)
        if (params.containsKey("type") && !params.get("type").isEmpty()) {
            String typeParam = params.get("type");
            try {
                TypeMemoire typeMemoire = TypeMemoire.valueOf(typeParam.toUpperCase());  // Assurez-vous que la valeur est en majuscule pour correspondre à l'enum
                spec = spec.and(MemoireSpecifications.withType(typeMemoire));
            } catch (IllegalArgumentException e) {
                // Si le type n'est pas valide, loggez l'erreur
                System.out.println("Type invalide : " + typeParam);
            }
        }

        return memoireRepository.findAll(spec);
    }

    // Méthode pour supprimer un mémoire par son ID
    public void deleteMemoire(Long id) {
        Memoire memoire = getMemoireById(id); // Vérifie si le mémoire existe
        memoireRepository.delete(memoire); // Supprime le mémoire
    }
    public List<Memoire> getMemoiresByType(TypeMemoire typeMemoire) {
        return memoireRepository.findByType(typeMemoire);
    }


    public Map<String, Map<String, List<Memoire>>> getMemoiresGroupes() {
        List<Memoire> memoires = getAllMemoires();
        Map<String, Map<String, List<Memoire>>> groupedMemoires = new HashMap<>();

        for (Memoire memoire : memoires) {
            String ufrNom = memoire.getUfr() != null ? memoire.getUfr().getNom() : "Inconnu";
            String departementNom = memoire.getDepartement() != null ? memoire.getDepartement().getNom() : "Inconnu";

            // Grouper par UFR puis par Département
            groupedMemoires
                    .computeIfAbsent(ufrNom, k -> new HashMap<>())
                    .computeIfAbsent(departementNom, k -> new ArrayList<>())
                    .add(memoire);
        }
        return groupedMemoires;
    }

    public List<Memoire> searchMemos(Specification<Memoire> spec) {
        return memoireRepository.findAll(spec);
    }

    public Map<Integer, Long> countMemosByYear() {
        List<Object[]> result = memoireRepository.countMemosGroupedByYear();
        Map<Integer, Long> memoiresParAnnee = new LinkedHashMap<>();
        for (Object[] row : result) {
            memoiresParAnnee.put((Integer) row[0], (Long) row[1]);
        }
        return memoiresParAnnee;
    }

    public long countMemosByType(TypeMemoire type) {
        return memoireRepository.findAllByType(type).size();
    }

    public Map<Integer, Long> countMemosByTypeAndYear(TypeMemoire type) {
        // Récupérer les années distinctes et compter par type
        List<Memoire> memoires = memoireRepository.findAllByType(type);
        return memoires.stream()
                .collect(Collectors.groupingBy(
                        Memoire::getAnnee,   // Regrouper par l'année
                        Collectors.counting() // Compter les mémoires dans chaque année
                ));
    }

    //liste de licences
    public Map<String, Map<String, List<Memoire>>> getMemoiresLicenceGroupes() {
        // Récupérer tous les mémoires de type Licence
        List<Memoire> memoiresLicence = memoireRepository.findByType(TypeMemoire.LICENCE);

        if (memoiresLicence == null || memoiresLicence.isEmpty()) {
            return new HashMap<>(); // Retourne un map vide s'il n'y a pas de mémoire
        }

        // Grouper les mémoires par UFR > Département
        return memoiresLicence.stream()
                .collect(Collectors.groupingBy(
                        memoire -> memoire.getFiliere().getDepartement().getUfr().getNom(),
                        Collectors.groupingBy(
                                memoire -> memoire.getFiliere().getDepartement().getNom()
                        )
                ));
    }

    //liste de masters
    public Map<String, Map<String, List<Memoire>>> getMemoiresMasterGroupes() {
        // Récupérer tous les mémoires de type Master
        List<Memoire> memoiresMaster = memoireRepository.findByType(TypeMemoire.MASTER);

        if (memoiresMaster == null || memoiresMaster.isEmpty()) {
            return new HashMap<>(); // Retourne un map vide s'il n'y a pas de mémoire
        }

        // Grouper les mémoires par UFR > Département
        return memoiresMaster.stream()
                .collect(Collectors.groupingBy(
                        memoire -> memoire.getFiliere().getDepartement().getUfr().getNom(),
                        Collectors.groupingBy(
                                memoire -> memoire.getFiliere().getDepartement().getNom()
                        )
                ));
    }

    //liste de these
    public Map<String, Map<String, List<Memoire>>> getMemoiresDoctoratGroupes() {
        // Récupérer tous les mémoires de type Doctorat
        List<Memoire> memoiresDoctorat = memoireRepository.findByType(TypeMemoire.DOCTORAT);

        if (memoiresDoctorat == null || memoiresDoctorat.isEmpty()) {
            return new HashMap<>(); // Retourne un map vide s'il n'y a pas de mémoire
        }

        // Grouper les mémoires par UFR > Département
        return memoiresDoctorat.stream()
                .collect(Collectors.groupingBy(
                        memoire -> memoire.getFiliere().getDepartement().getUfr().getNom(),
                        Collectors.groupingBy(
                                memoire -> memoire.getFiliere().getDepartement().getNom()
                        )
                ));
    }

    //Mon code pour recuperer les information de l'utilisateur qui est connecter
    public Utilisateur recherche_Utilisateur(String username){
        Utilisateur user = userRepository.findUtilisateurByUsername(username);
        return user;
    }
    public List<Memoire> findByAnneeAndType(int annee, String type) {
        try {
            // Conversion si nécessaire
            TypeMemoire typeEnum = TypeMemoire.valueOf(type.toUpperCase());
            return memoireRepository.findByAnneeAndType(annee, typeEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Type de mémoire invalide : " + type, e);
        }
    }


}
