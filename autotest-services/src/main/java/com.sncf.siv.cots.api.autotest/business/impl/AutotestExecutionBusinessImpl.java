package com.sncf.siv.cots.api.autotest.business.impl;

import com.sncf.siv.cots.api.autotest.model.dto.TestExecutionRequestDTO;
import com.sncf.siv.cots.api.autotest.model.dto.TestExecutionResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AutotestExecutionBusinessImpl {

    public TestExecutionResponseDTO executeTest(TestExecutionRequestDTO testRequest) {
        log.info("Début de l'exécution du test: {} sur le service: {}",
                testRequest.getTestName(), testRequest.getServiceUrl());

        LocalDateTime startTime = LocalDateTime.now();
        TestExecutionResponseDTO response = new TestExecutionResponseDTO();

        response.setTestName(testRequest.getTestName());
        response.setStartTime(startTime);

        try {
            executeTestByType(testRequest, response);
            response.setStatus("SUCCESS");

        } catch (Exception e) {
            log.error("Erreur lors de l'exécution du test: {}", testRequest.getTestName(), e);
            response.setStatus("ERROR");
            response.setMessage("Erreur lors de l'exécution: " + e.getMessage());
        }

        LocalDateTime endTime = LocalDateTime.now();
        response.setEndTime(endTime);
        response.setExecutionTimeMs(
                java.time.Duration.between(startTime, endTime).toMillis()
        );

        log.info("Test {} terminé avec le statut: {}", testRequest.getTestName(), response.getStatus());
        return response;
    }

    private void executeTestByType(TestExecutionRequestDTO testRequest, TestExecutionResponseDTO response) {
        Map<String, Object> details = new HashMap<>();

        String testType = testRequest.getTestType() != null ? testRequest.getTestType().toUpperCase() : "DEFAULT";

        switch (testType) {
            case "HEALTH_CHECK":
                log.info("Simulation d'un health check sur: {}", testRequest.getServiceUrl());
                details.put("testType", "HEALTH_CHECK");
                details.put("targetUrl", testRequest.getServiceUrl() + "/api/health/check");
                details.put("expectedStatus", "NO_CONTENT");
                details.put("actualStatus", "NO_CONTENT");
                details.put("healthCheckResult", "Service disponible (simulation)");
                response.setHttpStatusCode(204);
                response.setMessage("Test exécuté avec succès (implémentation dummy)");
                break;

            case "API_TEST":
                log.info("Simulation d'un test API sur: {}", testRequest.getServiceUrl());
                details.put("testType", "API_TEST");
                details.put("targetUrl", testRequest.getServiceUrl());
                details.put("apiCallsExecuted", 3);
                details.put("apiCallsSuccessful", 3);
                response.setHttpStatusCode(200);
                response.setMessage("Test exécuté avec succès (implémentation dummy)");
                break;

            default:
                log.info("Simulation d'un test par défaut sur: {}", testRequest.getServiceUrl());
                details.put("testType", "DEFAULT");
                details.put("targetUrl", testRequest.getServiceUrl());
                details.put("message", "Test basique d'accessibilité du service (simulation)");
                response.setHttpStatusCode(200);
                response.setMessage("Test exécuté avec succès (implémentation dummy)");
        }

        details.put("mode", "SIMULATION");
        details.put("timestamp", LocalDateTime.now());
        response.setDetails(details);
    }
}