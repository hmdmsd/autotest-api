package com.sncf.siv.cots.api.autotest.cucumber.stepdefinitions;

import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sncf.mobilite.vpr.cots.cucumber.api.common.config.SpringWebCucumberTestConfig;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Step definitions pour les tests TNR_SC05 - Consumer ancien service
 * Contient uniquement les step definitions spécifiques à TNR_SC05
 * Les step definitions communes sont définies dans ConsumerNouveauServiceStepDefs
 */
@Slf4j
public class ConsumerAncienServiceStepDefs extends SpringWebCucumberTestConfig {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${test.consumer.api.base-url:http://localhost:8082/consumer-api}")
    private String CONSUMER_API_BASE_URL;

    private ResponseEntity<String> lastConsumerResponse;

    // ========== Consumer HTTP Request Steps spécifiques TNR_SC05 ==========

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametres numeroCourse \"([^\"]*)\" codeService \"([^\"]*)\" dateDebutPeriode \"([^\"]*)\" dateFinPeriode \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametresCoursePTA(String url, String numeroCourse, String codeService,
                                                                 String dateDebutPeriode, String dateFinPeriode) {
        try {
            String fullUrl = CONSUMER_API_BASE_URL + url +
                    "?numeroCourse=" + URLEncoder.encode(numeroCourse, StandardCharsets.UTF_8) +
                    "&codeService=" + URLEncoder.encode(codeService, StandardCharsets.UTF_8) +
                    "&dateDebutPeriode=" + URLEncoder.encode(dateDebutPeriode, StandardCharsets.UTF_8) +
                    "&dateFinPeriode=" + URLEncoder.encode(dateFinPeriode, StandardCharsets.UTF_8);

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

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametres numeroCourse \"([^\"]*)\" codeService \"([^\"]*)\" typeCourse \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametresCoursePTP(String url, String numeroCourse, String codeService, String typeCourse) {
        try {
            String fullUrl = CONSUMER_API_BASE_URL + url +
                    "?numeroCourse=" + URLEncoder.encode(numeroCourse, StandardCharsets.UTF_8) +
                    "&codeService=" + URLEncoder.encode(codeService, StandardCharsets.UTF_8) +
                    "&typeCourse=" + URLEncoder.encode(typeCourse, StandardCharsets.UTF_8);

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

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametres numeroCourse \"([^\"]*)\" indicateurFer \"([^\"]*)\" codeCompagnieTransporteur \"([^\"]*)\" dateDebutPeriode \"([^\"]*)\" dateFinPeriode \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametresCoursesCommerciales(String url, String numeroCourse, String indicateurFer,
                                                                           String codeCompagnieTransporteur, String dateDebutPeriode, String dateFinPeriode) {
        try {
            String fullUrl = CONSUMER_API_BASE_URL + url +
                    "?numeroCourse=" + URLEncoder.encode(numeroCourse, StandardCharsets.UTF_8) +
                    "&indicateurFer=" + URLEncoder.encode(indicateurFer, StandardCharsets.UTF_8) +
                    "&codeCompagnieTransporteur=" + URLEncoder.encode(codeCompagnieTransporteur, StandardCharsets.UTF_8) +
                    "&dateDebutPeriode=" + URLEncoder.encode(dateDebutPeriode, StandardCharsets.UTF_8) +
                    "&dateFinPeriode=" + URLEncoder.encode(dateFinPeriode, StandardCharsets.UTF_8);

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

    @When("^Requete HTTP consumer POST URL \"([^\"]*)\" avec json \"([^\"]*)\"$")
    public void requeteHTTPConsumerPostUrlAvecJson(String url, String jsonFilePath) {
        try {
            String jsonContent = Files.readString(Paths.get("src/test/resources/" + jsonFilePath));

            String fullUrl = CONSUMER_API_BASE_URL + url;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer valid-test-token");

            HttpEntity<String> request = new HttpEntity<>(jsonContent, headers);

            lastConsumerResponse = restTemplate.exchange(fullUrl, HttpMethod.POST, request, String.class);

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
            throw new RuntimeException(String.format("Erreur lors de l'appel HTTP consumer POST %s: %s",
                    url, e.getMessage()), e);
        }
    }

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametres dateDebutPeriode \"([^\"]*)\" dateFinPeriode \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametresDatePeriode(String url, String dateDebutPeriode, String dateFinPeriode) {
        try {
            String fullUrl = CONSUMER_API_BASE_URL + url +
                    "?dateDebutPeriode=" + URLEncoder.encode(dateDebutPeriode, StandardCharsets.UTF_8) +
                    "&dateFinPeriode=" + URLEncoder.encode(dateFinPeriode, StandardCharsets.UTF_8);

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

    @When("^Requete HTTP consumer GET URL \"([^\"]*)\" avec parametres dateDebutPeriode \"([^\"]*)\" dateFinPeriode \"([^\"]*)\" codeTransporteurResponsable \"([^\"]*)\"$")
    public void requeteHTTPConsumerGetUrlAvecParametresDatePeriodeEtCodeTransporteur(String url, String dateDebutPeriode,
                                                                                     String dateFinPeriode, String codeTransporteurResponsable) {
        try {
            String fullUrl = CONSUMER_API_BASE_URL + url +
                    "?dateDebutPeriode=" + URLEncoder.encode(dateDebutPeriode, StandardCharsets.UTF_8) +
                    "&dateFinPeriode=" + URLEncoder.encode(dateFinPeriode, StandardCharsets.UTF_8) +
                    "&codeTransporteurResponsable=" + URLEncoder.encode(codeTransporteurResponsable, StandardCharsets.UTF_8);

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

}