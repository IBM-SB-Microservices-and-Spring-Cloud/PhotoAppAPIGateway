package com.photoapp.api.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import reactor.core.publisher.Mono;


@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>{


	@Autowired
	Environment environment;
	
	public AuthorizationHeaderFilter() {
		super(Config.class);
	}
	
//	@Autowired
//	public AuthorizationHeaderFilter(Environment environment) {
//		super();
//		this.environment = environment;
//	}

	public static class Config{
		//Put config props
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain)->{
			ServerHttpRequest serverHttpRequest = exchange.getRequest();
			if(!serverHttpRequest.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange,"No Authorization Header", HttpStatus.UNAUTHORIZED);
			}
			
			String authorization = serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = authorization.replaceAll("Bearer", "");
			if(!isJWTValid(jwt)) {
				return onError(exchange,"Invalid JWT Token", HttpStatus.UNAUTHORIZED);
			}
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String string, HttpStatus httpStatus) {
		ServerHttpResponse res = exchange.getResponse();
		res.setStatusCode(httpStatus);
		return res.setComplete();
	}
	
	private boolean isJWTValid(String jwtToken) {
		boolean retVal = false;
		String tokenSecret = environment.getProperty("token.secret");
		String userId = null;
		
		try {
			userId = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(jwtToken).getBody().getSubject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(userId != null)
			retVal = true;
		return retVal;
	}

}
