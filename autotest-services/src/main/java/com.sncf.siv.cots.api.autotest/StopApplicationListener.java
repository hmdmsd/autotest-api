package com.sncf.siv.cots.api.autotest;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

/**
 * Listener called when the application is shutting down
 *
 * This component logs application shutdown information.
 */
@Component
public class StopApplicationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopApplicationListener.class);

    @Autowired
    BuildProperties buildProperties;

    @PreDestroy
    public void onDestroy() {
        LOGGER.info("==============================================================");
        LOGGER.info("Application {} arrêtée en version {}", buildProperties.getName(), buildProperties.getVersion());
        LOGGER.info("==============================================================");
    }
}