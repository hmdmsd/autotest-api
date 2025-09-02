package com.sncf.siv.cots.api.autotest.cucumber.config;

import com.sncf.siv.cots.api.autotest.AutotestServer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sncf.mobilite.vpr.cots.cucumber.api.common.config.SpringWebCucumberTestConfig;

@ActiveProfiles({"test", "standalone"})
@SpringBootTest(classes = {AutotestServer.class, MongoBasedTestConfig.class, AutotestApiStepDefsConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class SpringIntegrationTestConfig extends SpringWebCucumberTestConfig {
}