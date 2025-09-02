package com.sncf.siv.cots.api.autotest.business.impl;

import com.sncf.siv.cots.api.autotest.model.dto.ServiceHealthDTO;
import com.sncf.siv.cots.api.autotest.model.dto.ServicesHealthDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ServicesHealthBusinessImpl {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Map<String, ServiceConfig> servicesConfig = new HashMap<>();

    @Value("${cots.api.catalog.baseUrl}")
    private String catalogApiBaseUrl;

    @Value("${cots.api.consumer.baseUrl}")
    private String consumerApiBaseUrl;

    @Value("${cots.api.job.baseUrl}")
    private String jobApiBaseUrl;

    @Value("${cots.api.openam.baseUrl}")
    private String openamApiBaseUrl;

    @Value("${cots.api.provider.baseUrl}")
    private String providerApiBaseUrl;

    @Value("${cots.api.octopus.baseUrl}")
    private String octopusServicesBaseUrl;

    @Value("${cots.api.notifier.baseUrl}")
    private String notifierServicesBaseUrl;

    @Value("${cots.api.autotest.baseUrl}")
    private String autotestApiBaseUrl;

    @Value("${cots.api.replayer.baseUrl}")
    private String replayerApiBaseUrl;

    public ServicesHealthBusinessImpl() {
        initializeServices();
    }

    private void initializeServices() {
        servicesConfig.put("catalog-api", new ServiceConfig("catalog-api", catalogApiBaseUrl));
        servicesConfig.put("consumer-api", new ServiceConfig("consumer-api", consumerApiBaseUrl));
        servicesConfig.put("job-api", new ServiceConfig("job-api", jobApiBaseUrl));
        servicesConfig.put("openam-api", new ServiceConfig("openam-api", openamApiBaseUrl));
        servicesConfig.put("provider-api", new ServiceConfig("provider-api", providerApiBaseUrl));
        servicesConfig.put("octopus-services", new ServiceConfig("octopus-services", octopusServicesBaseUrl));
        servicesConfig.put("notifier-services", new ServiceConfig("notifier-services", notifierServicesBaseUrl));
        servicesConfig.put("autotest-api", new ServiceConfig("autotest-api", autotestApiBaseUrl));
        servicesConfig.put("replayer-api", new ServiceConfig("replayer-api", replayerApiBaseUrl));

        log.info("Services configuration initialized with {} services", servicesConfig.size());
    }

    public ServicesHealthDTO checkAllServices() {
        LocalDateTime startTime = LocalDateTime.now();
        long startMillis = System.currentTimeMillis();

        List<CompletableFuture<ServiceHealthDTO>> futures = new ArrayList<>();
        for (ServiceConfig config : servicesConfig.values()) {
            CompletableFuture<ServiceHealthDTO> future = CompletableFuture.supplyAsync(
                    () -> checkService(config), executor
            );
            futures.add(future);
        }

        List<ServiceHealthDTO> results = new ArrayList<>();
        for (CompletableFuture<ServiceHealthDTO> future : futures) {
            try {
                results.add(future.get(30, TimeUnit.SECONDS));
            } catch (Exception e) {
                log.error("Error checking service", e);
                ServiceHealthDTO errorResult = new ServiceHealthDTO();
                errorResult.setStatus("UNKNOWN");
                errorResult.setMessage("Timeout");
                errorResult.setTimestamp(LocalDateTime.now());
                results.add(errorResult);
            }
        }

        long totalTime = System.currentTimeMillis() - startMillis;
        return buildResponse(results, startTime, totalTime);
    }

    public ServiceHealthDTO checkSingleService(String serviceName) {
        ServiceConfig config = servicesConfig.get(serviceName);
        if (config == null) {
            ServiceHealthDTO result = new ServiceHealthDTO();
            result.setServiceName(serviceName);
            result.setStatus("UNKNOWN");
            result.setMessage("Service not configured");
            result.setTimestamp(LocalDateTime.now());
            return result;
        }
        return checkService(config);
    }

    private ServiceHealthDTO checkService(ServiceConfig config) {
        ServiceHealthDTO result = new ServiceHealthDTO();
        result.setServiceName(config.getName());
        result.setServiceUrl(config.getBaseUrl());
        result.setTimestamp(LocalDateTime.now());

        long startTime = System.currentTimeMillis();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(config.getHealthUrl(), String.class);
            long responseTime = System.currentTimeMillis() - startTime;

            result.setHttpStatusCode(response.getStatusCode().value());
            result.setResponseTimeMs(responseTime);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK) {
                result.setStatus("UP");
                result.setMessage("Service available");
            } else {
                result.setStatus("DOWN");
                result.setMessage("Unexpected status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            result.setResponseTimeMs(responseTime);
            result.setStatus("DOWN");
            result.setMessage("Service unreachable: " + e.getMessage());
        }

        return result;
    }

    private ServicesHealthDTO buildResponse(List<ServiceHealthDTO> results, LocalDateTime startTime, long totalTime) {
        ServicesHealthDTO response = new ServicesHealthDTO();
        response.setTimestamp(startTime);
        response.setTotalExecutionTimeMs(totalTime);
        response.setServiceResults(results);
        response.setTotalServices(results.size());

        long servicesUp = results.stream().mapToLong(r -> "UP".equals(r.getStatus()) ? 1 : 0).sum();
        response.setServicesUp((int) servicesUp);
        response.setServicesDown(results.size() - (int) servicesUp);

        if (servicesUp == results.size()) {
            response.setOverallStatus("ALL_UP");
        } else if (servicesUp == 0) {
            response.setOverallStatus("ALL_DOWN");
        } else {
            response.setOverallStatus("SOME_DOWN");
        }

        return response;
    }

    private static class ServiceConfig {
        @Getter
        private final String name;
        @Getter
        private final String baseUrl;

        public ServiceConfig(String name, String baseUrl) {
            this.name = name;
            this.baseUrl = baseUrl;
        }

        public String getHealthUrl() {
            return baseUrl + "/api/health/check";
        }
    }
}