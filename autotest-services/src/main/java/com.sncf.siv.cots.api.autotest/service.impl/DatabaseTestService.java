package com.sncf.siv.cots.api.autotest.service.impl;

import com.sncf.siv.cots.data.domain.Course;
import com.sncf.siv.cots.data.domain.etat.course.distribuable.EtatCourseDistribuable;
import com.sncf.siv.cots.data.domain.etat.course.productible.EtatCourseProductible;
import com.sncf.siv.cots.data.domain.dossier.evenement.DossierEvenement;
import com.sncf.siv.cots.data.domain.etatcourseobservee.EtatCourseObservee;
import com.sncf.siv.cots.data.repository.CourseRepository;
import com.sncf.siv.cots.data.repository.EtatCourseObserveeRepository;
import com.sncf.siv.cots.data.repository.springdata.DossierEvenementRepository;
import com.sncf.siv.cots.data.repository.springdata.EtatCourseDistribuableRepository;
import com.sncf.siv.cots.data.repository.springdata.EtatCourseProductibleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DatabaseTestService {

    @Autowired
    private EtatCourseDistribuableRepository etatCourseDistribuableRepository;

    @Autowired
    private EtatCourseProductibleRepository etatCourseProductibleRepository;

    @Autowired
    private DossierEvenementRepository dossierEvenementRepository;

    @Autowired
    private EtatCourseObserveeRepository etatCourseObserveeRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private CourseRepository courseRepository;

    // ========== ECD Operations ==========

    public boolean deleteEcdByIdImmuable(String idImmuable) {
        log.info("Suppression ECD: {}", idImmuable);
        try {
            EtatCourseDistribuable existingEcd = etatCourseDistribuableRepository.findFirstIdImmuable(idImmuable);
            if (existingEcd != null) {
                etatCourseDistribuableRepository.deleteById(existingEcd.getId());
                log.info("ECD {} supprimé", idImmuable);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Erreur suppression ECD {}", idImmuable, e);
            return false;
        }
    }

    public boolean verifyEcdExistsByIdImmuable(String idImmuable) {
        try {
            EtatCourseDistribuable ecd = etatCourseDistribuableRepository.findFirstIdImmuable(idImmuable);
            return ecd != null;
        } catch (Exception e) {
            log.error("Erreur vérification ECD {}", idImmuable, e);
            return false;
        }
    }

    public Optional<EtatCourseDistribuable> getEcdByIdImmuable(String idImmuable) {
        try {
            EtatCourseDistribuable ecd = etatCourseDistribuableRepository.findFirstIdImmuable(idImmuable);
            return Optional.ofNullable(ecd);
        } catch (Exception e) {
            log.error("Erreur récupération ECD {}", idImmuable, e);
            return Optional.empty();
        }
    }

    // ========== ECP Operations ==========

    public boolean deleteEcpByIdImmuable(String idImmuable) {
        log.info("Suppression ECP: {}", idImmuable);
        try {
            List<EtatCourseProductible> ecps = etatCourseProductibleRepository.findAllByIdImmuable(idImmuable);
            if (!ecps.isEmpty()) {
                for (EtatCourseProductible ecp : ecps) {
                    etatCourseProductibleRepository.deleteById(ecp.getId());
                }
                log.info("ECP(s) {} supprimé(s), nombre: {}", idImmuable, ecps.size());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Erreur suppression ECP {}", idImmuable, e);
            return false;
        }
    }

    public boolean verifyEcpExistsByIdImmuable(String idImmuable) {
        try {
            return etatCourseProductibleRepository.existsByIdImmuable(idImmuable);
        } catch (Exception e) {
            log.error("Erreur vérification ECP {}", idImmuable, e);
            return false;
        }
    }

    public Optional<EtatCourseProductible> getEcpByIdImmuable(String idImmuable) {
        try {
            List<EtatCourseProductible> ecps = etatCourseProductibleRepository.findAllByIdImmuable(idImmuable);
            return ecps.isEmpty() ? Optional.empty() : Optional.of(ecps.get(0));
        } catch (Exception e) {
            log.error("Erreur récupération ECP {}", idImmuable, e);
            return Optional.empty();
        }
    }

    // ========== DossierEvenement Operations ==========

    public boolean deleteDossierEvenementByIdentifiantDansSystemeCreateur(String identifiantDansSystemeCreateur) {
        log.info("Suppression DossierEvenement: {}", identifiantDansSystemeCreateur);
        try {
            DossierEvenement existingDossier = dossierEvenementRepository
                    .findBySystemeCreateurAndIdentifiantDansSystemeCreateur("2148", identifiantDansSystemeCreateur);

            if (existingDossier != null) {
                dossierEvenementRepository.deleteById(existingDossier.getId());
                log.info("DossierEvenement {} supprimé", identifiantDansSystemeCreateur);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Erreur suppression DossierEvenement {}", identifiantDansSystemeCreateur, e);
            return false;
        }
    }

    public boolean verifyDossierEvenementExistsByIdentifiantDansSystemeCreateur(String identifiantDansSystemeCreateur) {
        try {
            DossierEvenement dossier = dossierEvenementRepository
                    .findBySystemeCreateurAndIdentifiantDansSystemeCreateur("2148", identifiantDansSystemeCreateur);
            return dossier != null;
        } catch (Exception e) {
            log.error("Erreur vérification DossierEvenement {}", identifiantDansSystemeCreateur, e);
            return false;
        }
    }

    public Optional<DossierEvenement> getDossierEvenementByIdentifiantDansSystemeCreateur(String identifiantDansSystemeCreateur) {
        try {
            DossierEvenement dossier = dossierEvenementRepository
                    .findBySystemeCreateurAndIdentifiantDansSystemeCreateur("2148", identifiantDansSystemeCreateur);
            return Optional.ofNullable(dossier);
        } catch (Exception e) {
            log.error("Erreur récupération DossierEvenement {}", identifiantDansSystemeCreateur, e);
            return Optional.empty();
        }
    }

    // ========== EtatCourseObservee Operations ==========

    public boolean deleteEtatCourseObserveeByIdImmuable(String idImmuable) {
        log.info("Suppression EtatCourseObservee: {}", idImmuable);
        try {
            EtatCourseObservee existingEco = etatCourseObserveeRepository.findByIdImmuable(idImmuable);
            if (existingEco != null) {
                etatCourseObserveeRepository.deleteById(existingEco.getId());
                log.info("EtatCourseObservee {} supprimé", idImmuable);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Erreur suppression EtatCourseObservee {}", idImmuable, e);
            return false;
        }
    }

    public boolean verifyEtatCourseObserveeExistsByIdImmuable(String idImmuable) {
        try {
            EtatCourseObservee eco = etatCourseObserveeRepository.findByIdImmuable(idImmuable);
            return eco != null;
        } catch (Exception e) {
            log.error("Erreur vérification EtatCourseObservee {}", idImmuable, e);
            return false;
        }
    }

    public Optional<EtatCourseObservee> getEtatCourseObserveeByIdImmuable(String idImmuable) {
        try {
            EtatCourseObservee eco = etatCourseObserveeRepository.findByIdImmuable(idImmuable);
            return Optional.ofNullable(eco);
        } catch (Exception e) {
            log.error("Erreur récupération EtatCourseObservee {}", idImmuable, e);
            return Optional.empty();
        }
    }

    // ========== Course Operations ==========

    public boolean deleteCourseByIdImmuable(String idImmuable) {
        log.info("Suppression Course: {}", idImmuable);
        try {
            Query query = new Query(Criteria.where("idImmuable").is(idImmuable));

            var courseResult = mongoOperations.remove(query, "course");
            if (courseResult.getDeletedCount() > 0) {
                log.info("Course {} supprimée de 'course' ({} documents)", idImmuable, courseResult.getDeletedCount());
                return true;
            }

            var etatResult = mongoOperations.remove(query, "etatCourseProductible");
            if (etatResult.getDeletedCount() > 0) {
                log.info("Course {} supprimée de 'etatCourseProductible' ({} documents)", idImmuable, etatResult.getDeletedCount());
                return true;
            }

            log.info("Course {} non trouvée", idImmuable);
            return true;
        } catch (Exception e) {
            log.error("Erreur suppression Course {}", idImmuable, e);
            return false;
        }
    }

    public boolean deleteCourseOpeByIdImmuable(String idImmuable) {
        log.info("Suppression Course OPE: {}", idImmuable);
        try {
            List<Course> courses = courseRepository.getAllCoursesFromIdImmuable(idImmuable);
            courses.removeIf(course -> !"COURSE_OPE".equals(course.getType().toString()));

            if (!courses.isEmpty()) {
                for (Course course : courses) {
                    courseRepository.deleteById(course.getId());
                }
                log.info("Course(s) OPE {} supprimée(s), nombre: {}", idImmuable, courses.size());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Erreur suppression Course OPE {}", idImmuable, e);
            return false;
        }
    }

    public boolean verifyCourseExistsByIdImmuable(String idImmuable) {
        try {
            Query query = new Query(Criteria.where("idImmuable").is(idImmuable));

            return mongoOperations.exists(query, "course") ||
                    mongoOperations.exists(query, "etatCourseProductible") ||
                    mongoOperations.exists(query, "etatCourseDistribuable");
        } catch (Exception e) {
            log.error("Erreur vérification Course {}", idImmuable, e);
            return false;
        }
    }

    public String getCourseNumeroCourseByIdImmuable(String idImmuable) {
        try {
            Query query = new Query(Criteria.where("idImmuable").is(idImmuable));
            query.fields().include("numeroCourse").include("referenceCourse");

            Object result = mongoOperations.findOne(query, Object.class, "course");
            if (result != null && result instanceof org.bson.Document) {
                org.bson.Document doc = (org.bson.Document) result;
                String numeroCourse = doc.getString("numeroCourse");
                if (numeroCourse != null) return numeroCourse;
            }

            return null;
        } catch (Exception e) {
            log.error("Erreur récupération Course numeroCourse {}", idImmuable, e);
            return null;
        }
    }

    public boolean verifyCourseOpeExistsByIdImmuable(String idImmuable) {
        return verifyCourseExistsByIdImmuable(idImmuable);
    }

    public boolean verifyCoursePtpExistsByIdImmuable(String idImmuable) {
        return verifyCourseExistsByIdImmuable(idImmuable);
    }

    // ========== CourseCommerciale Operations ==========

    public boolean deleteCourseCommercialeByIdImmuable(String idImmuable) {
        log.info("Suppression CourseCommerciale: {}", idImmuable);
        try {
            Query query = new Query(Criteria.where("idImmuable").is(idImmuable));

            var result = mongoOperations.remove(query, "courseCommerciale");
            if (result.getDeletedCount() > 0) {
                log.info("CourseCommerciale {} supprimée ({} documents)", idImmuable, result.getDeletedCount());
                return true;
            }

            // Also check etatCourseDistribuable as backup
            result = mongoOperations.remove(query, "etatCourseDistribuable");
            if (result.getDeletedCount() > 0) {
                log.info("CourseCommerciale {} supprimée de 'etatCourseDistribuable' ({} documents)", idImmuable, result.getDeletedCount());
                return true;
            }

            return true;
        } catch (Exception e) {
            log.error("Erreur suppression CourseCommerciale {}", idImmuable, e);
            return false;
        }
    }

    public boolean verifyCourseCommercialeExistsByIdImmuable(String idImmuable) {
        try {
            Query query = new Query(Criteria.where("idImmuable").is(idImmuable));

            return mongoOperations.exists(query, "courseCommerciale") ||
                    mongoOperations.exists(query, "etatCourseDistribuable");
        } catch (Exception e) {
            log.error("Erreur vérification CourseCommerciale {}", idImmuable, e);
            return false;
        }
    }

    // ========== PTP Operations ==========

    public boolean deletePtpByDateAndCodeTransporteur(String dateApplication, String codeTransporteurResponsable) {
        log.info("Suppression PTP: dateApplication={}, codeTransporteur={}", dateApplication, codeTransporteurResponsable);
        try {
            try {
                java.time.LocalDate localDate = java.time.LocalDate.parse(dateApplication);
                java.util.Date date = java.sql.Date.valueOf(localDate);

                Query query2 = new Query(Criteria.where("dateApplication").is(date)
                        .and("codeTransporteurResponsable").is(codeTransporteurResponsable));
                var result2 = mongoOperations.remove(query2, "planTransportPerturbe");
                log.info("PTP supprimé par critères date ({} documents)", result2.getDeletedCount());
            } catch (Exception e) {
                log.info("Date parsing failed: {}", e.getMessage());
            }

            Query query3 = new Query(Criteria.where("dateApplication").regex(".*" + dateApplication + ".*")
                    .and("codeTransporteurResponsable").is(codeTransporteurResponsable));
            var result3 = mongoOperations.remove(query3, "planTransportPerturbe");
            log.info("PTP supprimé par regex date ({} documents)", result3.getDeletedCount());

            return true;
        } catch (Exception e) {
            log.error("Erreur suppression PTP dateApplication={}, codeTransporteur={}",
                    dateApplication, codeTransporteurResponsable, e);
            return false;
        }
    }

    public boolean verifyPtpExistsByDateAndCodeTransporteur(String dateApplication, String codeTransporteurResponsable) {
        try {
            Query query1 = new Query(Criteria.where("dateApplication").is(dateApplication)
                    .and("codeTransporteurResponsable").is(codeTransporteurResponsable));
            boolean exists1 = mongoOperations.exists(query1, "planTransportPerturbe");

            try {
                java.time.LocalDate localDate = java.time.LocalDate.parse(dateApplication);
                java.util.Date date = java.sql.Date.valueOf(localDate);

                Query query2 = new Query(Criteria.where("dateApplication").is(date)
                        .and("codeTransporteurResponsable").is(codeTransporteurResponsable));
                boolean exists2 = mongoOperations.exists(query2, "planTransportPerturbe");

                return exists1 || exists2;
            } catch (Exception e) {
                return exists1;
            }
        } catch (Exception e) {
            log.error("Erreur vérification PTP dateApplication={}, codeTransporteur={}",
                    dateApplication, codeTransporteurResponsable, e);
            return false;
        }
    }

    public boolean verifyCoursePtaExists(String numeroCourse, String codeCompagnieTransporteur, String dateCirculation) {
        try {
            Query query1 = new Query(Criteria.where("numeroCourse").is(numeroCourse)
                    .and("codeCompagnieTransporteur").is(codeCompagnieTransporteur));

            Query query2 = new Query(Criteria.where("referenceCourse.numeroCourse").is(numeroCourse)
                    .and("referenceCourse.codeCompagnieTransporteur").is(codeCompagnieTransporteur));

            return mongoOperations.exists(query1, "etatCourseProductible") ||
                    mongoOperations.exists(query1, "course") ||
                    mongoOperations.exists(query2, "courseCommerciale") ||
                    mongoOperations.exists(query2, "etatCourseDistribuable");
        } catch (Exception e) {
            log.error("Erreur vérification Course PTA numeroCourse={}, codeCompagnie={}",
                    numeroCourse, codeCompagnieTransporteur, e);
            return false;
        }
    }

    // ========== Cleanup Methods ==========

    public void cleanupTnrSc02TestData() {
        log.info("Nettoyage données TNR_SC02");
        deleteEcpByIdImmuable("2148:1187-G-88865-20251130");
        deleteEcdByIdImmuable("2148:1187-F-230105-2025-11-28");
        deleteEcpByIdImmuable("2148:1187-F-8363-20251023");
        deleteDossierEvenementByIdentifiantDansSystemeCreateur("CU25SCN01T01");
        deleteEtatCourseObserveeByIdImmuable("2148:8080-F-1187-2025-05-05");
        log.info("Nettoyage TNR_SC02 terminé");
    }

    public void cleanupTnrSc03TestData() {
        log.info("Nettoyage données TNR_SC03");

        // Clean up in proper order to avoid dependency issues
        deleteCourseByIdImmuable("2148:1187-F-8080-20251202");
        deleteCourseByIdImmuable("2148:1187-F-140402-20251202");
        deleteCourseByIdImmuable("2148:1187-F-8824-20251110");
        deleteCourseByIdImmuable("2148:1187-F-11998-20251129");
        deleteCourseCommercialeByIdImmuable("2148:1187-F-170201-2025-10-24");

        deletePtpByDateAndCodeTransporteur("2025-12-02", "5G");

        try {
            Query cleanupQuery = new Query(Criteria.where("systemeCreateur").is("2148")
                    .and("dateApplication").is("2025-12-02"));
            var cleanupResult = mongoOperations.remove(cleanupQuery, "planTransportPerturbe");
            log.info("Additional PTP cleanup: {} documents removed", cleanupResult.getDeletedCount());

            mongoOperations.remove(cleanupQuery, "journeePTP");
        } catch (Exception e) {
            log.warn("Additional PTP cleanup failed", e);
        }

        log.info("Nettoyage TNR_SC03 terminé");
    }
    public void cleanupTnrSc04TestData() {
        log.info("Nettoyage données TNR_SC04");
        deleteEtatCourseObserveeByIdImmuable("2148:CU22SC02T01-F-1187-2025-12-31");
        deleteEcdByIdImmuable("2148:1187-F-835023-20251228");

        deleteDossierEvenementByIdentifiantDansSystemeCreateur("5D-2025-09-15");

        try {
            Query cleanupQuery = new Query(Criteria.where("systemeCreateur").is("2148")
                    .and("codeTransporteurResponsable").is("5D"));
            var cleanupResult = mongoOperations.remove(cleanupQuery, "dossierEvenement");
            log.info("Additional DossierEvenement cleanup for 5D: {} documents removed", cleanupResult.getDeletedCount());
        } catch (Exception e) {
            log.warn("Additional DossierEvenement cleanup failed", e);
        }

        deleteEcpByIdImmuable("2148:1187-F-2811-20251207");
        log.info("Nettoyage TNR_SC04 terminé");
    }

    /**
     * Nettoyage des données de test pour TNR_SC05
     */
    public void cleanupTnrSc05TestData() {
        log.info("Nettoyage données TNR_SC05");

        deleteDossierEvenementByIdentifiantDansSystemeCreateur("5T-2025-12-02");
        deleteDossierEvenementByIdentifiantDansSystemeCreateur("5J-2025-12-14");
        deleteDossierEvenementByIdentifiantDansSystemeCreateur("5J-2025-12-15");
        deleteDossierEvenementByIdentifiantDansSystemeCreateur("5H-2025-12-16");
        deleteDossierEvenementByIdentifiantDansSystemeCreateur("5H-2025-12-17");
        deleteDossierEvenementByIdentifiantDansSystemeCreateur("5J-2025-12-16");

        deleteEcpByIdImmuable("2148:1187-F-WN986-20250301");
        deleteEcpByIdImmuable("2148:1187-F-88865-20251130");
        deleteEcpByIdImmuable("2849:20250318-0001");
        deleteEcpByIdImmuable("2148:1187-F-11224-20251130");
        deleteEcpByIdImmuable("2148:1187-F-11225-20251130");
        deleteEcpByIdImmuable("2148:1187-F-11223-20251130");
        deleteEcpByIdImmuable("2147:1187-F-11224-20251130");
        deleteEcpByIdImmuable("2148:1187-F-11224-20251129");
        deleteEcpByIdImmuable("2148:1187-F-56520-20250116");
        deleteEcpByIdImmuable("2148:1187-F-56522-20250116");
        deleteEcpByIdImmuable("2148:1187-F-56524-20250116");
        deleteEcpByIdImmuable("2148:1187-F-56520-20250117");
        deleteEcpByIdImmuable("2148:1187-F-56522-20250117");
        deleteEcpByIdImmuable("2148:1187-F-56524-20250117");
        deleteEcpByIdImmuable("2148:1187-F-9876-20260831");

        deleteCourseByIdImmuable("2148:1187-F-WN986-20250301");
        deleteCourseByIdImmuable("2148:1187-F-88865-20251130");
        deleteCourseByIdImmuable("2148:1187-F-9876-20260831");

        deleteCourseOpeByIdImmuable("2148:1187-F-11999-20251202");
        deleteCourseOpeByIdImmuable("2849:20250318-0001");
        deleteCourseOpeByIdImmuable("2148:1187-F-11224-20251130");
        deleteCourseOpeByIdImmuable("2148:1187-F-11225-20251130");
        deleteCourseOpeByIdImmuable("2148:1187-F-11223-20251130");
        deleteCourseOpeByIdImmuable("2147:1187-F-11224-20251130");
        deleteCourseOpeByIdImmuable("2148:1187-F-11224-20251129");
        deleteCourseOpeByIdImmuable("2148:1187-F-9876-20260831");

        deleteCourseCommercialeByIdImmuable("2148:1187-F-081702-20251223");

        deletePtpByDateAndCodeTransporteur("2025-12-02", "5G");
        deletePtpByDateAndCodeTransporteur("2025-12-02", "5T");
        deletePtpByDateAndCodeTransporteur("2025-12-14", "5J");
        deletePtpByDateAndCodeTransporteur("2025-12-15", "5J");
        deletePtpByDateAndCodeTransporteur("2025-12-16", "5H");
        deletePtpByDateAndCodeTransporteur("2025-12-16", "5J");
        deletePtpByDateAndCodeTransporteur("2025-12-17", "5H");

        try {
            Query cleanupQuery = new Query(Criteria.where("systemeCreateur").is("2148")
                    .and("codeTransporteurResponsable").in("5T", "5J", "5H", "5G", "4T"));
            var cleanupResult = mongoOperations.remove(cleanupQuery, "planTransportPerturbe");
            log.info("Additional PTP cleanup TNR_SC05: {} documents removed", cleanupResult.getDeletedCount());

            mongoOperations.remove(cleanupQuery, "journeePTP");

            Query cleanupTTR = new Query(Criteria.where("systemeCreateur").is("TTR"));
            mongoOperations.remove(cleanupTTR, "courseOPE");
        } catch (Exception e) {
            log.warn("Additional TNR_SC05 cleanup failed", e);
        }

        log.info("Nettoyage TNR_SC05 terminé");
    }
    // ========== Debug Methods ==========

    public void debugListAllObjects() {
        log.info("=== DEBUG: Collections count ===");
        String[] collections = {
                "arrondiRetard", "catalog", "collectionTest", "course", "courseCommerciale",
                "debutDdmTransco", "dossierEvenement", "ecpOctopus", "erreurTranscoVersDevt",
                "erreurTranscoVersEcd", "erreurTranscoVersEcp", "etatCourseDistribuable",
                "etatCourseObservee", "etatCourseProductible", "lock", "notification",
                "planTransportPerturbe"
        };

        for (String collection : collections) {
            try {
                long count = mongoOperations.getCollection(collection).countDocuments();
                if (count > 0) {
                    log.info("{}: {} documents", collection, count);
                }
            } catch (Exception e) {
                log.debug("Collection {} inaccessible", collection);
            }
        }
    }
}