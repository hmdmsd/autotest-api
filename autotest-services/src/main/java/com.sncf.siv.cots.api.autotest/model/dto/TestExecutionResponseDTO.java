package com.sncf.siv.cots.api.autotest.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO de réponse pour l'exécution de tests automatisés
 */
@Data
public class TestExecutionResponseDTO {

    /**
     * Nom du test exécuté
     */
    private String testName;

    /**
     * Statut de l'exécution (SUCCESS, FAILURE, ERROR)
     */
    private String status;

    /**
     * Message descriptif du résultat
     */
    private String message;

    /**
     * Horodatage de début d'exécution
     */
    private LocalDateTime startTime;

    /**
     * Horodatage de fin d'exécution
     */
    private LocalDateTime endTime;

    /**
     * Durée d'exécution en millisecondes
     */
    private Long executionTimeMs;

    /**
     * Détails additionnels du test
     */
    private Map<String, Object> details;

    /**
     * Code de retour HTTP si applicable
     */
    private Integer httpStatusCode;
}