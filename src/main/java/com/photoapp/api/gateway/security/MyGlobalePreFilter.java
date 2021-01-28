package com.photoapp.api.gateway.security;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class MyGlobalePreFilter implements GlobalFilter {

	final Logger logger = org.slf4j.LoggerFactory.getLogger(MyGlobalePreFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("######## In MyGlobalePreFilter");

		String string = exchange.getRequest().getPath().toString();
		logger.info("URL Path: " + string);
		HttpHeaders headers = exchange.getRequest().getHeaders();
		List<String> list = headers.get(HttpHeaders.ACCEPT);
		logger.info("Header Accept: " + list.toString());

		return chain.filter(exchange);
	}

}
