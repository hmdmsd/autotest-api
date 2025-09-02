package com.sncf.siv.cots.api.autotest.cucumber.stepdefinitions;

import io.cucumber.java.en.Then;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import sncf.mobilite.vpr.cots.cucumber.api.common.config.SpringWebCucumberTestConfig;
import sncf.mobilite.vpr.cots.cucumber.api.common.stepdefinitions.HttpStepDefs;

public class ServicesHealthStepDefs extends SpringWebCucumberTestConfig {

    @Resource
    private HttpStepDefs httpStepDefs;

    @Then("^la réponse contient des données de santé$")
    public void la_reponse_contient_des_donnees_de_sante() throws Exception {
        String response = httpStepDefs.getJsonResponse();
        Assertions.assertNotNull(response, "La réponse ne doit pas être nulle");
        Assertions.assertFalse(response.isEmpty(), "La réponse ne doit pas être vide");
        Assertions.assertTrue(response.contains("{") || response.contains("["),
                "La réponse doit contenir des données JSON");
    }

    @Then("^la réponse contient le service \"([^\"]*)\"$")
    public void la_reponse_contient_le_service(String serviceName) throws Exception {
        String response = httpStepDefs.getJsonResponse();
        Assertions.assertNotNull(response, "La réponse ne doit pas être nulle");
        Assertions.assertTrue(response.contains(serviceName),
                "La réponse doit contenir le nom du service: " + serviceName);
    }

    @Then("^le service \"([^\"]*)\" est en état \"([^\"]*)\"$")
    public void le_service_est_en_etat(String serviceName, String expectedStatus) throws Exception {
        String response = httpStepDefs.getJsonResponse();
        Assertions.assertNotNull(response, "La réponse ne doit pas être nulle");
        Assertions.assertTrue(response.contains(expectedStatus),
                String.format("La réponse doit contenir le statut %s pour le service %s",
                        expectedStatus, serviceName));
    }
}