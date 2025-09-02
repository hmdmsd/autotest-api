package com.sncf.siv.cots.api.autotest.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * DTO de requête pour l'exécution de tests automatisés
 */
@Data
public class TestExecutionRequestDTO {

    /**
     * Nom du test à exécuter
     */
    private String testName;

    /**
     * URL du service COTS à tester
     */
    private String serviceUrl;

    /**
     * Type de test à exécuter (HEALTH_CHECK, API_TEST, etc.)
     */
    private String testType;

    /**
     * Paramètres additionnels pour le test
     */
    private Map<String, Object> parameters;

    /**
     * Timeout en secondes pour l'exécution du test
     */
    private Integer timeoutSeconds = 30;

    /**
     * Indique si le test doit être exécuté de manière asynchrone
     */
    private Boolean async = false;
}