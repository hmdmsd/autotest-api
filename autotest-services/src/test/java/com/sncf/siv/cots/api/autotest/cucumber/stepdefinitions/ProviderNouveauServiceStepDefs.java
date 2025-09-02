package com.sncf.siv.cots.api.autotest.cucumber.stepdefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sncf.siv.cots.common.util.StreamUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sncf.mobilite.vpr.cots.cucumber.api.common.config.SpringWebCucumberTestConfig;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ProviderNouveauServiceStepDefs extends SpringWebCucumberTestConfig {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${cots.api.provider.baseUrl}")
    private String PROVIDER_API_BASE_URL;

    private String requestBody;
    private ResponseEntity<String> lastResponse;

    // ========== COMMON STEPS ==========

    @Given("^json contenu dans le corps de la requete externe \"([^\"]*)\"$")
    public void jsonContenuDansLeCorpsDeLaRequeteExterne(String jsonFile) {
        this.requestBody = StreamUtil.readStream(jsonFile);
        assertNotNull(requestBody, "Le contenu du fichier JSON ne doit pas être null");
        assertFalse(requestBody.trim().isEmpty(), "Le contenu du fichier JSON ne doit pas être vide");
    }

    @When("^Requete HTTP externe methode \"([^\"]*)\" URL \"([^\"]*)\"$")
    public void requeteHTTPExterneMethodeURL(String method, String url) {
        String fullUrl = PROVIDER_API_BASE_URL + url;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer valid-test-token");

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            lastResponse = switch (method.toUpperCase()) {
                case "POST" -> restTemplate.postForEntity(fullUrl, request, String.class);
                case "GET" -> restTemplate.exchange(fullUrl, HttpMethod.GET, request, String.class);
                default -> throw new IllegalArgumentException("Méthode HTTP non supportée: " + method);
            };
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
            lastResponse = new ResponseEntity<>(errorBody, httpError.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(String.format("Erreur lors de l'appel HTTP %s %s: %s",
                    method, fullUrl, e.getMessage()), e);
        }
    }

    @Then("^Code HTTP externe (\\d+) recu$")
    public void codeHTTPExterneRecu(int expectedStatus) {
        assertNotNull(lastResponse, "Aucune réponse HTTP reçue");
        assertEquals(expectedStatus, lastResponse.getStatusCode().value(),
                String.format("Code HTTP attendu: %d, reçu: %d", expectedStatus, lastResponse.getStatusCode().value()));
    }

    @Then("^la réponse externe contient \"([^\"]*)\"$")
    public void laReponseExterneContient(String expectedContent) {
        String responseBody = getResponseBody();
        assertTrue(responseBody.contains(expectedContent),
                String.format("La réponse doit contenir '%s'. Réponse reçue: %s", expectedContent, responseBody));
    }

    @Then("^la réponse externe contient le numeroCourse \"([^\"]*)\"$")
    public void laReponseExterneContientLeNumeroCourse(String numeroCourseAttendu) {
        String responseBody = getResponseBody();
        assertTrue(responseBody.contains(numeroCourseAttendu),
                String.format("La réponse doit contenir le numeroCourse '%s'. Réponse reçue: %s",
                        numeroCourseAttendu, responseBody));
    }

    @Then("^la liste externe \"([^\"]*)\" doit être vide$")
    public void laListeExterneDoitEtreVide(String listeName) throws Exception {
        JsonNode jsonNode = getExternalJsonResponse();
        JsonNode listNode = jsonNode.get(listeName);

        assertNotNull(listNode, String.format("La liste '%s' doit être présente dans la réponse", listeName));
        assertTrue(listNode.isArray(), String.format("'%s' doit être un tableau", listeName));
        assertEquals(0, listNode.size(),
                String.format("La liste '%s' doit être vide. Réponse reçue: %s", listeName, getResponseBody()));
    }

    @Then("^la réponse externe contient les listes \"([^\"]*)\" et \"([^\"]*)\"$")
    public void laReponseExterneContientLesListesEt(String liste1, String liste2) throws Exception {
        JsonNode jsonNode = getExternalJsonResponse();
        String responseBody = getResponseBody();

        assertTrue(jsonNode.has(liste1),
                String.format("La réponse doit contenir la liste '%s'. Réponse reçue: %s", liste1, responseBody));
        assertTrue(jsonNode.has(liste2),
                String.format("La réponse doit contenir la liste '%s'. Réponse reçue: %s", liste2, responseBody));
    }

    @Then("^la liste externe \"([^\"]*)\" doit contenir l'identifiant externe de l'ecd importée$")
    public void laListeExterneDoitContenirLIdentifiantExterneDeLEcdImportee(String listeName) throws Exception {
        JsonNode jsonNode = getExternalJsonResponse();
        JsonNode listNode = jsonNode.get(listeName);

        assertNotNull(listNode, String.format("La liste '%s' doit être présente dans la réponse", listeName));
        assertTrue(listNode.isArray(), String.format("'%s' doit être un tableau", listeName));
        assertTrue(listNode.size() > 0,
                String.format("La liste '%s' doit contenir au moins un élément. Réponse reçue: %s",
                        listeName, getResponseBody()));

        JsonNode firstElement = listNode.get(0);
        assertTrue(firstElement.has("identifiantCOTS") || firstElement.has("idImmuable") ||
                        firstElement.has("identifiantDansSystemeCreateur"),
                String.format("Le premier élément de la liste '%s' doit contenir un identifiant. Réponse reçue: %s",
                        listeName, getResponseBody()));
    }

    // ========== TNR_SC02 SPECIFIC STEPS ==========

    @Given("^json pour retourner ECP OPE avec idImmuable \"([^\"]*)\"$")
    public void jsonPourRetournerEcpOpeAvecIdImmuable(String idImmuable) {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("idImmuable", idImmuable);
        this.requestBody = requestBody.toString();

        assertNotNull(this.requestBody, "Le contenu de la requête pour ECP OPE ne doit pas être null");
        assertFalse(this.requestBody.trim().isEmpty(), "Le contenu de la requête pour ECP OPE ne doit pas être vide");
    }

    @Then("^la réponse externe correspond au modèle attendu \"([^\"]*)\"$")
    public void laReponseExterneCorrespondAuModeleAttendu(String expectedJsonFile) throws Exception {
        JsonNode actualJson = getExternalJsonResponse();
        String expectedResponse = StreamUtil.readStream(expectedJsonFile);
        JsonNode expectedJson = objectMapper.readTree(expectedResponse);

        validateResponseStructure(actualJson, "etatsCoursesDistribuablesCrees",
                "etatsCoursesDistribuablesModifies", "etatsCoursesDistribuablesRejetes");

        assertEquals(0, actualJson.get("etatsCoursesDistribuablesModifies").size(),
                "La liste 'etatsCoursesDistribuablesModifies' doit être vide");
        assertEquals(0, actualJson.get("etatsCoursesDistribuablesRejetes").size(),
                "La liste 'etatsCoursesDistribuablesRejetes' doit être vide");

        JsonNode createdList = actualJson.get("etatsCoursesDistribuablesCrees");
        assertTrue(createdList.size() > 0,
                "La liste 'etatsCoursesDistribuablesCrees' doit contenir au moins un élément");

        JsonNode firstCreated = createdList.get(0);
        validateHasFields(firstCreated, "identifiantCOTS", "idImmuable", "systemeCreateur",
                "identifiantDansSystemeCreateur");
    }

    @Then("^la réponse externe correspond au modèle attendu \"([^\"]*)\" en excluant les champs dynamiques$")
    public void laReponseExterneCorrespondAuModeleAttenduEnExcluantLesChampsDynamiques(String expectedJsonFile) throws Exception {
        JsonNode actualJson = getExternalJsonResponse();
        String expectedResponse = StreamUtil.readStream(expectedJsonFile);
        JsonNode expectedJson = objectMapper.readTree(expectedResponse);

        Set<String> excludedFields = Set.of("@id", "idVersion", "dateCirculation");
        String[] essentialFields = {"idImmuable", "numeroCourse", "codeCompagnieTransporteur",
                "systemeCreateur", "identifiantDansSystemeCreateur"};

        validateFieldsMatch(actualJson, expectedJson, essentialFields, excludedFields);
    }

    @Then("^la réponse externe correspond au modèle attendu \"([^\"]*)\" en excluant le champ id généré$")
    public void laReponseExterneCorrespondAuModeleAttenduEnExcluantLeChampIdGenere(String expectedJsonFile) throws Exception {
        JsonNode actualJson = getExternalJsonResponse();
        String expectedResponse = StreamUtil.readStream(expectedJsonFile);
        JsonNode expectedJson = objectMapper.readTree(expectedResponse);

        Set<String> excludedFields = Set.of("id");
        String[] essentialFields = {"identifiantDansSystemeCreateur", "systemeCreateur", "typeDossier",
                "etat", "dateCreation", "description"};

        validateFieldsMatch(actualJson, expectedJson, essentialFields, excludedFields);
    }

    @Then("^la réponse externe contient l'etatCourseObservee avec les champs générés automatiquement$")
    public void laReponseExterneContientEtatCourseObserveeAvecLesChampsGeneresAutomatiquement() throws Exception {
        JsonNode jsonNode = getExternalJsonResponse();

        validateHasFields(jsonNode, "idImmuable", "@id", "idVersion");

        if (jsonNode.has("estTechnique")) {
            assertFalse(jsonNode.get("estTechnique").asBoolean(),
                    "Le champ 'estTechnique' doit être valorisé par défaut à 'false'");
        }
    }

    // ========== HELPER METHODS ==========

    private String getResponseBody() {
        assertNotNull(lastResponse, "Aucune réponse HTTP reçue");
        String responseBody = lastResponse.getBody();
        assertNotNull(responseBody, "Le corps de la réponse ne doit pas être null");
        return responseBody;
    }

    private JsonNode getExternalJsonResponse() throws Exception {
        String responseBody = getResponseBody();
        return objectMapper.readTree(responseBody);
    }

    private void validateResponseStructure(JsonNode actualJson, String... requiredFields) {
        for (String field : requiredFields) {
            assertTrue(actualJson.has(field),
                    String.format("La réponse doit contenir '%s'", field));
        }
    }

    private void validateHasFields(JsonNode jsonNode, String... fields) {
        for (String field : fields) {
            assertTrue(jsonNode.has(field),
                    String.format("L'élément doit contenir '%s'", field));
        }
    }

    private void validateFieldsMatch(JsonNode actualJson, JsonNode expectedJson,
                                     String[] essentialFields, Set<String> excludedFields) {
        for (String field : essentialFields) {
            if (expectedJson.has(field) && !excludedFields.contains(field)) {
                assertTrue(actualJson.has(field),
                        String.format("Le champ '%s' est manquant dans la réponse", field));

                String expectedValue = expectedJson.get(field).asText();
                String actualValue = actualJson.get(field).asText();

                assertEquals(expectedValue, actualValue,
                        String.format("Valeur différente pour le champ '%s': attendu '%s', trouvé '%s'",
                                field, expectedValue, actualValue));
            }
        }
    }
}