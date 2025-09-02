package com.sncf.siv.cots.api.autotest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * Listener called once the application is ready
 *
 * This component logs application startup information including:
 * - Property sources being used
 * - Application name and version
 */
@Slf4j
@Component
public class StartupApplicationListener {

    @Autowired
    BuildProperties buildProperties;

    private final ConfigurableEnvironment env;

    /**
     * Constructor
     *
     * @param env environment properties
     */
    public StartupApplicationListener(ConfigurableEnvironment env) {
        this.env = env;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyLogInfo() {

        // List all property sources
        env.getPropertySources().iterator()
                .forEachRemaining(p -> log.info("Property source : {}", p.getName()));

        // Liste des informations sur l'application
        log.info("");
        log.info("==============================================================");
        log.info("Application {} started in version {}", buildProperties.getName(), buildProperties.getVersion());
        log.info("==============================================================");
        log.info("");
    }
}