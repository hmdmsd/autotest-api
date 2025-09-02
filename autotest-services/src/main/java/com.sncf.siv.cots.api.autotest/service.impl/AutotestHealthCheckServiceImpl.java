package com.sncf.siv.cots.api.autotest.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sncf.mobilite.vpr.cots.api.definition.service.HealthCheckService;

/**
 * Implémentation du service de vérification de l'état de santé pour Auto Test
 */
@RestController
@RequestMapping("/api/health")
public class AutotestHealthCheckServiceImpl implements HealthCheckService {

    @GetMapping({"/check", "/check/"})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Override
    public String getHealthCheck() {
        return null;
    }
}