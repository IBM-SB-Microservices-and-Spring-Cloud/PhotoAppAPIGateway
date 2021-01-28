package com.photoapp.api.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

@Configuration
public class MyGlobalFiltersConfiguration {

	Logger logger = LoggerFactory.getLogger(MyGlobalFiltersConfiguration.class);

	@Order(1)
	@Bean
	public GlobalFilter mySecondPreFilter() {

		return (exchange, chain) -> {
			logger.info("In MyGlobalFiltersConfiguration::mySecondPreFilter Pre-filter");
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {

				logger.info("In MyGlobalFiltersConfiguration::mySecondPreFilter Post-filter");
			}));
		};
	}
	
	@Order(2)
	@Bean
	public GlobalFilter myThirdPreFilter() {

		return (exchange, chain) -> {
			logger.info("In MyGlobalFiltersConfiguration::myThirdPreFilter Pre-filter");
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {

				logger.info("In MyGlobalFiltersConfiguration::myThirdPreFilter Post-filter");
			}));
		};
	}
}
