package com.sncf.siv.cots.api.autotest.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.sncf.siv.cots.api.autotest.cucumber,sncf.mobilite.vpr.cots.cucumber.api.common.stepdefinitions,sncf.mobilite.vpr.cots")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/resources/com/sncf/siv/cots/api/autotest/cucumber")
public class AutotestApiCucumberTestConfig {
}