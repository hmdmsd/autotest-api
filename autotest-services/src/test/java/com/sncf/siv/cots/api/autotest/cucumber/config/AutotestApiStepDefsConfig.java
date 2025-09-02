package com.sncf.siv.cots.api.autotest.cucumber.config;

import com.sncf.siv.cots.api.autotest.business.impl.AutotestExecutionBusinessImpl;
import com.sncf.siv.cots.api.autotest.business.impl.ServicesHealthBusinessImpl;
import com.sncf.siv.cots.api.autotest.service.impl.DatabaseTestService;
import com.sncf.siv.cots.openam.config.OpenAmSecurity;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sncf.mobilite.vpr.cots.api.common.advice.ExceptionAdvice;
import sncf.mobilite.vpr.cots.cucumber.api.common.objects.ApiTestsContext;

@TestConfiguration
@Profile("test")
@ComponentScan(basePackages = {
        "sncf.mobilite.vpr.cots.cucumber.api.common.stepdefinitions",
        "com.sncf.siv.cots.common",
        "com.sncf.siv.cots.api.autotest",
        "com.sncf.siv.cots.data.repository"
},
        basePackageClasses = ExceptionAdvice.class)
@EnableMongoRepositories(basePackages = "com.sncf.siv.cots.data.repository.springdata")
@EnableConfigurationProperties(OpenAmSecurity.class)
public class AutotestApiStepDefsConfig {

    @Bean
    @Primary
    public ServicesHealthBusinessImpl servicesHealthBusiness() {
        return new ServicesHealthBusinessImpl();
    }

    @Bean
    @Primary
    public AutotestExecutionBusinessImpl autotestExecutionBusiness() {
        return new AutotestExecutionBusinessImpl();
    }

    @Bean
    @Primary
    public DatabaseTestService databaseTestService() {
        return new DatabaseTestService();
    }

    @Bean(name = "apiTestsContext")
    public ApiTestsContext apiTestsContext() {
        return new ApiTestsContext();
    }

    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        return executor;
    }
}