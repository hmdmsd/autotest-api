package com.sncf.siv.cots.api.autotest.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServicesHealthDTO {

    private String overallStatus;
    private Integer totalServices;
    private Integer servicesUp;
    private Integer servicesDown;
    private LocalDateTime timestamp;
    private Long totalExecutionTimeMs;
    private List<ServiceHealthDTO> serviceResults;
}