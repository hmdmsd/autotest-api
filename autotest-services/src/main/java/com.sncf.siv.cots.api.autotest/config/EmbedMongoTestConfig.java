package com.sncf.siv.cots.api.autotest.config;

import com.sncf.siv.cots.data.config.CustomConverters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@Import({CustomConverters.class})
@EnableMongoAuditing(dateTimeProviderRef = "customZonedDateTimeProvider")
@Slf4j
public class EmbedMongoTestConfig {

    public EmbedMongoTestConfig() {
        log.info("TestMongoConfig initialized");
    }
}
