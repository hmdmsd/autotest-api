package com.sncf.siv.cots.api.autotest.config;

import com.sncf.siv.cots.openam.config.OpenAmSecurity;
import com.sncf.siv.cots.openam.enumeration.OpenAmEnums;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import sncf.mobilite.vpr.cots.api.common.advice.CotsControllerAdvice;
import sncf.mobilite.vpr.cots.api.common.advice.ExceptionAdvice;
import sncf.mobilite.vpr.cots.api.common.advice.TechnicalAdvice;
import sncf.mobilite.vpr.cots.api.common.config.ApiConfig;
import sncf.mobilite.vpr.cots.api.common.filter.ApiFilter;
import sncf.mobilite.vpr.cots.api.common.security.error.CustomAccessDeniedHandler;
import sncf.mobilite.vpr.cots.api.common.security.error.CustomAuthenticationExceptionHandler;
import sncf.mobilite.vpr.cots.api.common.security.error.WebAppAuthenticationEntryPoint;
import sncf.mobilite.vpr.cots.api.common.security.filter.OpenAmPreAuthenticatedFilter;
import sncf.mobilite.vpr.socle.commun.security.config.BasicAuthWebSecurityConfigurerAdapter;
import sncf.mobilite.vpr.socle.commun.security.config.EurekaClientConfig;

@ComponentScan(basePackages = {
		"sncf.mobilite.vpr.cots.api.common.security",
		"sncf.mobilite.vpr.socle.commun.security"
})
@Import({
		ApiConfig.class,
		BasicAuthWebSecurityConfigurerAdapter.class,
		CotsControllerAdvice.class,
		EurekaClientConfig.class,
		ExceptionAdvice.class,
		TechnicalAdvice.class
})
public class WebServiceConfig {

	@Configuration
	@Order(5)
	public static class ApiSecurityConfiguration {

		private static final Logger LOGGER = LoggerFactory.getLogger(ApiSecurityConfiguration.class);

		@Resource
		private OpenAmSecurity openAmSecurity;

		@Resource
		private OpenAmPreAuthenticatedFilter openAmPreAuthenticatedProcessingFilter;

		@Resource
		private CustomAccessDeniedHandler customAccessDeniedHandler;

		@Resource
		private CustomAuthenticationExceptionHandler customAuthenticationExceptionHandler;

		@Resource
		private WebAppAuthenticationEntryPoint webAppAuthenticationEntryPoint;

		private static final String BASE_URL_AUTOTEST = "/api";

		@Bean
		public SecurityFilterChain configureOpenAm(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {

			if (openAmSecurity.isActivated()) {
				LOGGER.info("Security enable");
				http
						.securityMatcher(mvc.pattern(BASE_URL_AUTOTEST + "/**"))
						.csrf(AbstractHttpConfigurer::disable)
						.sessionManagement(sessionManagement -> sessionManagement
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						.addFilterBefore(new ApiFilter(), ChannelProcessingFilter.class)
						.addFilter(openAmPreAuthenticatedProcessingFilter)
						.authorizeHttpRequests(requests -> requests
								.requestMatchers(mvc.pattern(BASE_URL_AUTOTEST + "/health/check")).permitAll()
								.requestMatchers(mvc.pattern(HttpMethod.GET, BASE_URL_AUTOTEST + "/services/health/**")).permitAll()
								.requestMatchers(mvc.pattern(HttpMethod.POST, BASE_URL_AUTOTEST + "/test/execute")).hasAnyAuthority(OpenAmEnums.CotsRole.COURSE_WRITE.name())
								.anyRequest().authenticated())
						.exceptionHandling(exceptionHandling -> exceptionHandling
								.defaultAccessDeniedHandlerFor(customAccessDeniedHandler, mvc.pattern(BASE_URL_AUTOTEST + "/**"))
								.defaultAuthenticationEntryPointFor(webAppAuthenticationEntryPoint, mvc.pattern(BASE_URL_AUTOTEST + "/test/execute"))
								.defaultAuthenticationEntryPointFor(customAuthenticationExceptionHandler, mvc.pattern(BASE_URL_AUTOTEST + "/services/**")));
			} else {
				LOGGER.info("Security disable");
				http
						.csrf(AbstractHttpConfigurer::disable)
						.sessionManagement(sessionManagement -> sessionManagement
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						.addFilterBefore(new ApiFilter(), ChannelProcessingFilter.class)
						.addFilter(openAmPreAuthenticatedProcessingFilter)
						.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());

			}
			return http.build();
		}

	}

}