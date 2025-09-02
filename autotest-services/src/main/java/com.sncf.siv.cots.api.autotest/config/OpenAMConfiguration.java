package com.sncf.siv.cots.api.autotest.config;

import com.sncf.siv.cots.openam.client.config.OpenAmClientConfig;
import com.sncf.siv.cots.openam.config.OpenAmSecurity;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(OpenAmClientConfig.class)
@ComponentScan(basePackages = {
    "com.sncf.siv.cots.openam"
})
@EnableConfigurationProperties(OpenAmSecurity.class)
public class OpenAMConfiguration {
}
