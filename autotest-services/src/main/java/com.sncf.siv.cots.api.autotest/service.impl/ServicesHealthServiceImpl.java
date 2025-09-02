package com.sncf.siv.cots.api.autotest.service.impl;

import com.sncf.siv.cots.api.autotest.business.impl.ServicesHealthBusinessImpl;
import com.sncf.siv.cots.api.autotest.model.dto.ServiceHealthDTO;
import com.sncf.siv.cots.api.autotest.model.dto.ServicesHealthDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ServicesHealthServiceImpl {

    private static final String REQUEST_URI_SERVICES_HEALTH = "/api/services/health";

    @Autowired
    private ServicesHealthBusinessImpl servicesHealthBusiness;

    @GetMapping(value = {REQUEST_URI_SERVICES_HEALTH, REQUEST_URI_SERVICES_HEALTH + "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public ServicesHealthDTO checkAllServicesHealth() {
        log.info("Checking all services health");
        return servicesHealthBusiness.checkAllServices();
    }

    @GetMapping(value = {REQUEST_URI_SERVICES_HEALTH + "/{serviceName}", REQUEST_URI_SERVICES_HEALTH + "/{serviceName}/"})
    @ResponseStatus(value = HttpStatus.OK)
    public ServiceHealthDTO checkServiceHealth(@PathVariable String serviceName) {
        log.info("Checking service health: {}", serviceName);
        return servicesHealthBusiness.checkSingleService(serviceName);
    }
}