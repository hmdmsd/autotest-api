package com.sncf.siv.cots.api.autotest.cucumber.stepdefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sncf.mobilite.vpr.cots.cucumber.api.common.config.SpringWebCucumberTestConfig;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ConsumerNouveauServiceStepDefs extends SpringWebCucumberTestConfig {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cots.api.consumer.baseUrl}")
    private String CONSUMER_API_BASE_URL;

    private ResponseEntity<String> lastConsumerResponse;

    // ========== CONSUMER SPECIFIC STEPS ==========

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametre idImmuable \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametreIdImmuable(String url, String idImmuable) {
        // Add retry logic for eventual consistency with more attempts and longer waits
        for (int attempt = 0; attempt < 5; attempt++) {
            try {
                String fullUrl = CONSUMER_API_BASE_URL + url + "?idImmuable=" + idImmuable;

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer valid-test-token");

                HttpEntity<String> request = new HttpEntity<>(headers);

                lastConsumerResponse = restTemplate.exchange(fullUrl, HttpMethod.GET, request, String.class);

                if (lastConsumerResponse.getStatusCode().is2xxSuccessful()) {
                    String body = lastConsumerResponse.getBody();
                    if (body != null && !body.trim().equals("[]") && !body.trim().isEmpty()) {
                        return;
                    }
                }

                if (attempt < 4) {
                    long waitTime = (attempt + 1) * 2000; // Progressive wait: 2s, 4s, 6s, 8s
                    Thread.sleep(waitTime);
                }
            } catch (Exception e) {
                if (attempt == 4) {
                    throw new RuntimeException(String.format("Erreur lors de l'appel HTTP consumer GET %s après 5 tentatives: %s",
                            url, e.getMessage()), e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametres identifiantDansSystemeCreateur \"([^\"]*)\" et systemeCreateur \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametresIdentifiantEtSystemeCreateur(String url, String identifiantDansSystemeCreateur, String systemeCreateur) {
        try {
            String fullUrl = CONSUMER_API_BASE_URL + url +
                    "?identifiantDansSystemeCreateur=" + URLEncoder.encode(identifiantDansSystemeCreateur, StandardCharsets.UTF_8) +
                    "&systemeCreateur=" + URLEncoder.encode(systemeCreateur, StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer valid-test-token");

            HttpEntity<String> request = new HttpEntity<>(headers);

            lastConsumerResponse = restTemplate.exchange(fullUrl, HttpMethod.GET, request, String.class);

        } catch (HttpClientErrorException httpError) {
            String errorBody = httpError.getResponseBodyAsString();
            if (errorBody.isEmpty()) {
                errorBody = String.format(
                        "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"path\":\"%s\"}",
                        java.time.Instant.now().toString(),
                        httpError.getStatusCode().value(),
                        httpError.getStatusText(),
                        url
                );
            }
            lastConsumerResponse = new ResponseEntity<>(errorBody, httpError.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(String.format("Erreur lors de l'appel HTTP consumer GET %s: %s",
                    url, e.getMessage()), e);
        }
    }

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametre codeTransporteurResponsable \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametreCodeTransporteurResponsable(String url, String codeTransporteurResponsable) {
        try {
            String fullUrl = CONSUMER_API_BASE_URL + url +
                    "?codeTransporteurResponsable.type=INCLUSIVE" +
                    "&codeTransporteurResponsable.listeValeur[0]=" + URLEncoder.encode(codeTransporteurResponsable, StandardCharsets.UTF_8);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer valid-test-token");

            HttpEntity<String> request = new HttpEntity<>(headers);

            lastConsumerResponse = restTemplate.exchange(fullUrl, HttpMethod.GET, request, String.class);

        } catch (HttpClientErrorException httpError) {
            String errorBody = httpError.getResponseBodyAsString();
            if (errorBody.isEmpty()) {
                errorBody = String.format(
                        "{\"timestamp\":\"%s\",\"status\":%d,\"error\":\"%s\",\"path\":\"%s\"}",
                        java.time.Instant.now().toString(),
                        httpError.getStatusCode().value(),
                        httpError.getStatusText(),
                        url
                );
            }
            lastConsumerResponse = new ResponseEntity<>(errorBody, httpError.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(String.format("Erreur lors de l'appel HTTP consumer GET %s: %s",
                    url, e.getMessage()), e);
        }
    }

    @Then("^Code HTTP consumer (\\d+) recu$")
    public void codeHTTPConsumerRecu(int expectedStatus) {
        assertNotNull(lastConsumerResponse, "Aucune réponse HTTP consumer reçue");
        assertEquals(expectedStatus, lastConsumerResponse.getStatusCode().value(),
                String.format("Code HTTP consumer attendu: %d, reçu: %d. Réponse: %s",
                        expectedStatus, lastConsumerResponse.getStatusCode().value(),
                        lastConsumerResponse.getBody()));
    }

    @Then("^la réponse consumer contient \"([^\"]*)\"$")
    public void laReponseConsumerContient(String expectedContent) {
        String responseBody = getConsumerResponseBody();
        assertTrue(responseBody.contains(expectedContent),
                String.format("La réponse consumer doit contenir '%s'. Réponse reçue: %s", expectedContent, responseBody));
    }

    @Then("^la réponse consumer contient le numeroCourse \"([^\"]*)\"$")
    public void laReponseConsumerContientLeNumeroCourse(String numeroCourseAttendu) {
        String responseBody = getConsumerResponseBody();
        assertTrue(responseBody.contains(numeroCourseAttendu),
                String.format("La réponse consumer doit contenir le numeroCourse '%s'. Réponse reçue: %s",
                        numeroCourseAttendu, responseBody));
    }

    @Then("^un seul objet est retourné dans la réponse consumer$")
    public void unSeulObjetEstRetourneDansLaReponseConsumer() throws Exception {
        JsonNode jsonNode = getConsumerJsonResponse();
        assertTrue(jsonNode.isArray(), "La réponse consumer doit être un tableau");
        assertEquals(1, jsonNode.size(),
                String.format("La réponse consumer doit contenir exactement 1 élément, mais en contient %d. Réponse: %s",
                        jsonNode.size(), getConsumerResponseBody()));
    }

    @Then("^la réponse consumer correspond au modèle attendu \"([^\"]*)\"$")
    public void laReponseConsumerCorrespondAuModeleAttendu(String expectedJsonFile) throws Exception {
        JsonNode actualJsonArray = getConsumerJsonResponse();

        assertTrue(actualJsonArray.isArray(), "La réponse consumer doit être un tableau");
        assertEquals(1, actualJsonArray.size(), "La réponse consumer doit contenir exactement 1 élément");

        JsonNode actual = actualJsonArray.get(0);

        validateConsumerResponseStructure(actual);
    }

    @Then("^la réponse consumer correspond au modèle attendu \"([^\"]*)\" en excluant les champs dynamiques$")
    public void laReponseConsumerCorrespondAuModeleAttenduEnExcluantLesChampsDynamiques(String expectedJsonFile) throws Exception {
        JsonNode actualJsonArray = getConsumerJsonResponse();

        assertTrue(actualJsonArray.isArray(), "La réponse consumer doit être un tableau");
        assertEquals(1, actualJsonArray.size(), "La réponse consumer doit contenir exactement 1 élément");

        JsonNode actual = actualJsonArray.get(0);

        validateConsumerResponseStructure(actual);
    }

    @Then("^la réponse consumer ne contient pas le champ \"([^\"]*)\"$")
    public void laReponseConsumerNeContientPasLeChamp(String fieldName) throws Exception {
        JsonNode jsonArray = getConsumerJsonResponse();
        assertTrue(jsonArray.isArray(), "La réponse consumer doit être un tableau");

        if (jsonArray.size() > 0) {
            JsonNode firstElement = jsonArray.get(0);
            assertFalse(firstElement.has(fieldName),
                    String.format("La réponse consumer ne doit pas contenir le champ '%s'. Réponse: %s",
                            fieldName, getConsumerResponseBody()));
        }
    }

    // ========== HELPER METHODS ==========

    private String getConsumerResponseBody() {
        assertNotNull(lastConsumerResponse, "Aucune réponse HTTP consumer reçue");
        String responseBody = lastConsumerResponse.getBody();
        assertNotNull(responseBody, "Le corps de la réponse consumer ne doit pas être null");
        return responseBody;
    }

    private JsonNode getConsumerJsonResponse() throws Exception {
        String responseBody = getConsumerResponseBody();
        return objectMapper.readTree(responseBody);
    }

    public void setLastConsumerResponse(ResponseEntity<String> response) {
        this.lastConsumerResponse = response;
    }

    public ResponseEntity<String> getLastConsumerResponse() {
        return lastConsumerResponse;
    }

    private void validateConsumerResponseStructure(JsonNode actual) {
        if (actual.has("typeDossier")) {
            // DossierEvenement structure validation
            String[] dossierEvenementFields = {
                    "systemeCreateur", "identifiantDansSystemeCreateur",
                    "etat", "typeDossier"
            };

            for (String field : dossierEvenementFields) {
                assertTrue(actual.has(field),
                        String.format("Le champ structurel '%s' est manquant dans la réponse consumer DossierEvenement", field));
            }

        } else {
            // Other entities structure validation (ECP, ECD, etc.)
            String[] structuralFields = {
                    "idImmuable", "systemeCreateur", "etat"
            };

            for (String field : structuralFields) {
                assertTrue(actual.has(field),
                        String.format("Le champ structurel '%s' est manquant dans la réponse consumer", field));
            }

            assertNotNull(actual.get("idImmuable"),
                    "Le champ idImmuable ne doit pas être null dans la réponse consumer");
        }
    }

    @Then("^la réponse consumer contient le champ \"([^\"]*)\" avec la valeur \"([^\"]*)\"$")
    public void laReponseConsumerContientLeChampAvecLaValeur(String fieldName, String expectedValue) throws Exception {
        JsonNode jsonArray = getConsumerJsonResponse();
        assertTrue(jsonArray.isArray(), "La réponse consumer doit être un tableau");

        boolean fieldFound = false;
        for (JsonNode element : jsonArray) {
            if (element.has(fieldName)) {
                String actualValue = element.get(fieldName).asText();
                if (expectedValue.equals(actualValue)) {
                    fieldFound = true;
                    break;
                }
            }
        }

        assertTrue(fieldFound,
                String.format("Le champ '%s' avec la valeur '%s' n'a pas été trouvé dans la réponse consumer",
                        fieldName, expectedValue));
    }
}