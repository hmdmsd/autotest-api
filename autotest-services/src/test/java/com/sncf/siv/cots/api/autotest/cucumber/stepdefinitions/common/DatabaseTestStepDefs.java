package com.sncf.siv.cots.api.autotest.cucumber.stepdefinitions.common;

import com.sncf.siv.cots.api.autotest.service.impl.DatabaseTestService;
import com.sncf.siv.cots.data.domain.etat.course.distribuable.EtatCourseDistribuable;
import com.sncf.siv.cots.data.domain.etat.course.productible.EtatCourseProductible;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sncf.mobilite.vpr.cots.cucumber.api.common.config.SpringWebCucumberTestConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class DatabaseTestStepDefs extends SpringWebCucumberTestConfig {

    @Autowired
    private DatabaseTestService databaseTestService;

    // ========== Pre-test Cleanup Steps TNR_SC02 ==========

    @Given("^les données de test TNR_SC02 sont nettoyées$")
    public void lesDonneesDeTestTnrSc02SontNettoyees() {
        databaseTestService.cleanupTnrSc02TestData();
    }

    @Given("^l'ECP avec idImmuable \"([^\"]*)\" n'existe pas en base$")
    public void lEcpAvecIdImmuableNexistePasEnBase(String idImmuable) {
        databaseTestService.deleteEcpByIdImmuable(idImmuable);
        boolean exists = databaseTestService.verifyEcpExistsByIdImmuable(idImmuable);
        assertFalse(exists, "L'ECP avec idImmuable " + idImmuable + " ne devrait pas exister en base");
    }

    @Given("^l'ECD avec idImmuable \"([^\"]*)\" n'existe pas en base$")
    public void lEcdAvecIdImmuableNexistePasEnBase(String idImmuable) {
        databaseTestService.deleteEcdByIdImmuable(idImmuable);
        boolean exists = databaseTestService.verifyEcdExistsByIdImmuable(idImmuable);
        assertFalse(exists, "L'ECD avec idImmuable " + idImmuable + " ne devrait pas exister en base");
    }

    @Given("^le dossier événement avec identifiantDansSystemeCreateur \"([^\"]*)\" n'existe pas en base$")
    public void leDossierEvenementAvecIdentifiantDansSystemeCreateurNexistePasEnBase(String identifiantDansSystemeCreateur) {
        databaseTestService.deleteDossierEvenementByIdentifiantDansSystemeCreateur(identifiantDansSystemeCreateur);
        boolean exists = databaseTestService.verifyDossierEvenementExistsByIdentifiantDansSystemeCreateur(identifiantDansSystemeCreateur);
        assertFalse(exists, "Le DossierEvenement avec identifiantDansSystemeCreateur " + identifiantDansSystemeCreateur + " ne devrait pas exister en base");
    }

    @Given("^l'etatCourseObservee avec idImmuable \"([^\"]*)\" n'existe pas en base$")
    public void etatCourseObserveeAvecIdImmuableNexistePasEnBase(String idImmuable) {
        databaseTestService.deleteEtatCourseObserveeByIdImmuable(idImmuable);
        boolean exists = databaseTestService.verifyEtatCourseObserveeExistsByIdImmuable(idImmuable);
        assertFalse(exists, "L'EtatCourseObservee avec idImmuable " + idImmuable + " ne devrait pas exister en base");
    }

    // ========== Pre-test Cleanup Steps TNR_SC03 ==========

    @Given("^les données de test TNR_SC03 sont nettoyées$")
    public void lesDonneesDeTestTnrSc03SontNettoyees() {
        databaseTestService.cleanupTnrSc03TestData();
    }

    @Given("^la course avec idImmuable \"([^\"]*)\" n'existe pas en base$")
    public void laCourseAvecIdImmuableNexistePasEnBase(String idImmuable) {
        databaseTestService.deleteCourseByIdImmuable(idImmuable);
        boolean exists = databaseTestService.verifyCourseExistsByIdImmuable(idImmuable);
        assertFalse(exists, "La course avec idImmuable " + idImmuable + " ne devrait pas exister en base");
    }

    @Given("^la course OPE avec idImmuable \"([^\"]*)\" n'existe pas en base$")
    public void laCourseOpeAvecIdImmuableNexistePasEnBase(String idImmuable) {
        databaseTestService.deleteCourseOpeByIdImmuable(idImmuable);
        boolean exists = databaseTestService.verifyCourseOpeExistsByIdImmuable(idImmuable);
        assertFalse(exists, "La course OPE avec idImmuable " + idImmuable + " ne devrait pas exister en base");
    }

    @Given("^la course commerciale avec idImmuable \"([^\"]*)\" n'existe pas en base$")
    public void laCourseCommercialeAvecIdImmuableNexistePasEnBase(String idImmuable) {
        databaseTestService.deleteCourseCommercialeByIdImmuable(idImmuable);
        boolean exists = databaseTestService.verifyCourseCommercialeExistsByIdImmuable(idImmuable);
        assertFalse(exists, "La course commerciale avec idImmuable " + idImmuable + " ne devrait pas exister en base");
    }

    @Given("^le PTP avec dateApplication \"([^\"]*)\" et codeTransporteurResponsable \"([^\"]*)\" n'existe pas en base$")
    public void lePtpAvecDateApplicationEtCodeTransporteurResponsableNexistePasEnBase(String dateApplication, String codeTransporteurResponsable) {
        databaseTestService.deletePtpByDateAndCodeTransporteur(dateApplication, codeTransporteurResponsable);
        boolean exists = databaseTestService.verifyPtpExistsByDateAndCodeTransporteur(dateApplication, codeTransporteurResponsable);
        assertFalse(exists, "Le PTP avec dateApplication " + dateApplication + " et codeTransporteurResponsable " + codeTransporteurResponsable + " ne devrait pas exister en base");
    }

    @Given("^une course PTA avec numeroCourse \"([^\"]*)\" et codeCompagnieTransporteur \"([^\"]*)\" existe en base pour la date \"([^\"]*)\"$")
    public void uneCourse​PtaAvecNumeroCourseEtCodeCompagnieTransporteurExisteEnBasePourLaDate(String numeroCourse, String codeCompagnieTransporteur, String dateCirculation) {
        boolean exists = databaseTestService.verifyCoursePtaExists(numeroCourse, codeCompagnieTransporteur, dateCirculation);
        assertTrue(exists, "Une course PTA avec numeroCourse " + numeroCourse + " et codeCompagnieTransporteur " + codeCompagnieTransporteur + " devrait exister en base pour la date " + dateCirculation);
    }

    // ========== Pre-test Cleanup Steps TNR_SC04 ==========

    @Given("^les données de test TNR_SC04 sont nettoyées$")
    public void lesDonneesDeTestTnrSc04SontNettoyees() {
        databaseTestService.cleanupTnrSc04TestData();
    }

    // ========== Pre-test Cleanup Steps TNR_SC05 ==========

    @Given("^les données de test TNR_SC05 sont nettoyées$")
    public void lesDonneesDeTestTnrSc05SontNettoyees() {
        databaseTestService.cleanupTnrSc05TestData();
    }

    // ========== Database Verification Steps TNR_SC02 ==========

    @Then("^l'ECP avec idImmuable \"([^\"]*)\" doit être présent en base$")
    public void lEcpAvecIdImmuableDoitEtrePresentEnBase(String idImmuable) {
        // Add retry logic for eventual consistency - sometimes DB needs time to propagate
        boolean exists = false;
        for (int attempt = 0; attempt < 5; attempt++) {
            exists = databaseTestService.verifyEcpExistsByIdImmuable(idImmuable);
            if (exists) {
                break;
            }

            if (attempt < 4) {
                try {
                    long waitTime = (attempt + 1) * 1000; // Progressive wait: 1s, 2s, 3s, 4s
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        if (!exists) {
            // Additional debugging for TNR_SC04_T04
            databaseTestService.debugListAllObjects();
        }

        assertTrue(exists, "L'ECP avec idImmuable " + idImmuable + " devrait être présent en base");
    }

    @Then("^l'ECD avec idImmuable \"([^\"]*)\" doit être présent en base$")
    public void lEcdAvecIdImmuableDoitEtrePresentEnBase(String idImmuable) {
        // Add retry logic for eventual consistency
        boolean exists = false;
        for (int attempt = 0; attempt < 3; attempt++) {
            exists = databaseTestService.verifyEcdExistsByIdImmuable(idImmuable);
            if (exists) {
                break;
            }

            if (attempt < 2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        assertTrue(exists, "L'ECD avec idImmuable " + idImmuable + " devrait être présent en base");
    }

    @Then("^l'ECP avec idImmuable \"([^\"]*)\" doit avoir le numeroCourse \"([^\"]*)\"$")
    public void lEcpAvecIdImmuableDoitAvoirLeNumeroCourse(String idImmuable, String expectedNumeroCourse) {
        Optional<EtatCourseProductible> ecp = databaseTestService.getEcpByIdImmuable(idImmuable);
        assertTrue(ecp.isPresent(), "L'ECP avec idImmuable " + idImmuable + " devrait être présent en base");
        assertEquals(expectedNumeroCourse, ecp.get().getNumeroCourse(), "Le numeroCourse de l'ECP ne correspond pas");
    }

    @Then("^l'ECD avec idImmuable \"([^\"]*)\" doit avoir le numeroCourse \"([^\"]*)\"$")
    public void lEcdAvecIdImmuableDoitAvoirLeNumeroCourse(String idImmuable, String expectedNumeroCourse) {
        Optional<EtatCourseDistribuable> ecd = databaseTestService.getEcdByIdImmuable(idImmuable);
        assertTrue(ecd.isPresent(), "L'ECD avec idImmuable " + idImmuable + " devrait être présent en base");
        assertEquals(expectedNumeroCourse, ecd.get().getNumeroCourse(), "Le numeroCourse de l'ECD ne correspond pas");
    }

    @Then("^le dossier événement avec identifiantDansSystemeCreateur \"([^\"]*)\" doit être présent en base$")
    public void leDossierEvenementAvecIdentifiantDansSystemeCreateurDoitEtrePresentEnBase(String identifiantDansSystemeCreateur) {
        boolean exists = databaseTestService.verifyDossierEvenementExistsByIdentifiantDansSystemeCreateur(identifiantDansSystemeCreateur);
        assertTrue(exists, "Le DossierEvenement avec identifiantDansSystemeCreateur " + identifiantDansSystemeCreateur + " devrait être présent en base");
    }

    @Then("^l'etatCourseObservee avec idImmuable \"([^\"]*)\" doit être présent en base$")
    public void etatCourseObserveeAvecIdImmuableDoitEtrePresentEnBase(String idImmuable) {
        boolean exists = databaseTestService.verifyEtatCourseObserveeExistsByIdImmuable(idImmuable);
        assertTrue(exists, "L'EtatCourseObservee avec idImmuable " + idImmuable + " devrait être présent en base");
    }

    // ========== Database Verification Steps TNR_SC03 ==========

    @Then("^la course avec idImmuable \"([^\"]*)\" doit être présente en base$")
    public void laCourseAvecIdImmuableDoitEtrePresenteEnBase(String idImmuable) {
        boolean exists = databaseTestService.verifyCourseExistsByIdImmuable(idImmuable);
        assertTrue(exists, "La course avec idImmuable " + idImmuable + " devrait être présente en base");
    }

    @Then("^la course avec idImmuable \"([^\"]*)\" doit avoir le numeroCourse \"([^\"]*)\"$")
    public void laCourseAvecIdImmuableDoitAvoirLeNumeroCourse(String idImmuable, String expectedNumeroCourse) {
        String actualNumeroCourse = databaseTestService.getCourseNumeroCourseByIdImmuable(idImmuable);
        assertEquals(expectedNumeroCourse, actualNumeroCourse, "Le numeroCourse de la course ne correspond pas");
    }

    @Then("^la course PTP avec idImmuable \"([^\"]*)\" doit être présente en base$")
    public void laCoursePtpAvecIdImmuableDoitEtrePresenteEnBase(String idImmuable) {
        boolean exists = databaseTestService.verifyCoursePtpExistsByIdImmuable(idImmuable);
        assertTrue(exists, "La course PTP avec idImmuable " + idImmuable + " devrait être présente en base");
    }

    @Then("^la course OPE avec idImmuable \"([^\"]*)\" doit être présente en base$")
    public void laCourseOpeAvecIdImmuableDoitEtrePresenteEnBase(String idImmuable) {
        boolean exists = databaseTestService.verifyCourseOpeExistsByIdImmuable(idImmuable);
        assertTrue(exists, "La course OPE avec idImmuable " + idImmuable + " devrait être présente en base");
    }

    @Then("^la course commerciale avec idImmuable \"([^\"]*)\" doit être présente en base$")
    public void laCourseCommercialeAvecIdImmuableDoitEtrePresenteEnBase(String idImmuable) {
        boolean exists = databaseTestService.verifyCourseCommercialeExistsByIdImmuable(idImmuable);
        assertTrue(exists, "La course commerciale avec idImmuable " + idImmuable + " devrait être présente en base");
    }

    @Then("^le PTP avec dateApplication \"([^\"]*)\" et codeTransporteurResponsable \"([^\"]*)\" doit être présent en base$")
    public void lePtpAvecDateApplicationEtCodeTransporteurResponsableDoitEtrePresentEnBase(String dateApplication, String codeTransporteurResponsable) {
        boolean exists = databaseTestService.verifyPtpExistsByDateAndCodeTransporteur(dateApplication, codeTransporteurResponsable);
        assertTrue(exists, "Le PTP avec dateApplication " + dateApplication + " et codeTransporteurResponsable " + codeTransporteurResponsable + " devrait être présent en base");
    }

    // ========== Database Verification Steps TNR_SC04 ==========

    @Then("^l'objet avec idImmuable \"([^\"]*)\" est bien en base avant le test consumer$")
    public void objetAvecIdImmuableEstBienEnBaseAvantLeTestConsumer(String idImmuable) {
        try {
            Thread.sleep(2000); // Wait longer for DB consistency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean exists = databaseTestService.verifyEcpExistsByIdImmuable(idImmuable);
        assertTrue(exists, "L'objet avec idImmuable " + idImmuable + " devrait être présent en base avant le test consumer");
    }

    // ========== Database Verification Steps TNR_SC05 ==========

    // ========== Debug Steps ==========

    @Given("^je liste tous les objets en base pour debug$")
    public void jeListeTousLesObjetsEnBasePourDebug() {
        databaseTestService.debugListAllObjects();
    }
}