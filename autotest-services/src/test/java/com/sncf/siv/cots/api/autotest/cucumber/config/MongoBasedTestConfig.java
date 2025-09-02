package com.sncf.siv.cots.api.autotest.cucumber.config;

import com.sncf.siv.cots.api.autotest.config.EmbedMongoTestConfig;
import com.sncf.siv.cots.cucumber.openam.OpenAmTestUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({EmbedMongoTestConfig.class})
@ComponentScan(basePackageClasses = {
        OpenAmTestUtils.class
})
public class MongoBasedTestConfig {
}
