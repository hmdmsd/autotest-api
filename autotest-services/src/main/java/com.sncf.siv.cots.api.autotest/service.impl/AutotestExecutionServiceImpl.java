package com.sncf.siv.cots.api.autotest.service.impl;

import com.sncf.siv.cots.api.autotest.business.impl.AutotestExecutionBusinessImpl;
import com.sncf.siv.cots.api.autotest.model.dto.TestExecutionRequestDTO;
import com.sncf.siv.cots.api.autotest.model.dto.TestExecutionResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Implémentation du service d'exécution des tests automatisés
 */
@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AutotestExecutionServiceImpl {

    private static final String REQUEST_URI_EXECUTE_TEST = "/api/test/execute";

    @Autowired
    private AutotestExecutionBusinessImpl autoTestExecutionBusiness;

    @PostMapping(value = {REQUEST_URI_EXECUTE_TEST, REQUEST_URI_EXECUTE_TEST + "/"},
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public TestExecutionResponseDTO executeTest(@RequestBody TestExecutionRequestDTO testRequest) {

        log.info("Début de l'exécution du test: {}", testRequest.getTestName());

        TestExecutionResponseDTO response = autoTestExecutionBusiness.executeTest(testRequest);

        log.info("Test {} terminé avec le statut: {}", testRequest.getTestName(), response.getStatus());
        return response;
    }
}