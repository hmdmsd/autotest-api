package com.sncf.siv.cots.api.autotest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.data.mongo.MongoHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages = {
		"com.sncf.siv.cots.data.config",
		"com.sncf.siv.cots.data.repository"
})
@EnableMongoRepositories(basePackages = "com.sncf.siv.cots.data.repository")
public class DataConfig {

	@Bean
	@Autowired
	MongoHealthIndicator mongoDB(MongoTemplate template) {
		return new MongoHealthIndicator(template);
	}
}