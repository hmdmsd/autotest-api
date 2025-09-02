package com.sncf.siv.cots.api.autotest.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceHealthDTO {

    private String serviceName;
    private String serviceUrl;
    private Integer port;
    private String status;
    private Integer httpStatusCode;
    private Long responseTimeMs;
    private String message;
    private LocalDateTime timestamp;
}