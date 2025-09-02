package com.sncf.siv.cots.api.autotest.config;

import com.sncf.siv.cots.common.factory.ValidatorFactory;
import jakarta.validation.Validator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.annotation.EnableRetry;
import sncf.mobilite.vpr.cots.api.common.bean.WebProperties;

@Import({
        DataConfig.class,
        // BusinessConfig.class,
        OpenAMConfiguration.class,
        WebServiceConfig.class,
        // KafkaProducerConfig.class,
        // KafkaConnexionConfiguration.class
})
@EnableConfigurationProperties
@EnableRetry
public class MainConfiguration {

    @Bean(name = "webProperties")
    @ConfigurationProperties(prefix = "webapp")
    public WebProperties webProperties() {
        return new WebProperties();
    }

    @Primary
    @Bean(name = "validatorProvider")
    public Validator validatorProvider() {
        return ValidatorFactory.get();
    }

}
