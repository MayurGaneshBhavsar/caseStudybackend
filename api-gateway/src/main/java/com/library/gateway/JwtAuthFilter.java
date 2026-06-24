package com.library.gateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

	@Autowired
	private JwtUtil jwtUtil;

	private static final List<String> PUBLIC_PATHS = List.of(
			"/auth/register",
			"/auth/login"
	);

	private static final List<String> ADMIN_PATHS = List.of(
			"/members/renew",
			"/borrows/pay-fine"
	);

	private boolean isAdminOnlyPath(String path, String method) {
		// exact admin-only borrow paths
		if (path.equals("/borrows/fines")) return true;
		// block /borrows/fines only if NOT a member-specific sub-path
		if (path.startsWith("/borrows/fines") && !path.startsWith("/borrows/fines/member")) return true;
		// book mutations
		if (path.startsWith("/books") && !method.equals("GET")) return true;
		// get all members
		if (path.equals("/members") && method.equals("GET")) return true;
		// delete member
		if (path.startsWith("/members/") && method.equals("DELETE")) return true;
		// other admin paths
		if (ADMIN_PATHS.stream().anyMatch(path::startsWith)) return true;
		return false;
	}
	
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		String method = exchange.getRequest().getMethod().name();

		if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
			return chain.filter(exchange);
		}

		String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		String token = authHeader.substring(7);

		if (!jwtUtil.isTokenValid(token)) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		String role = jwtUtil.extractRole(token);

		boolean isAdminPath = isAdminOnlyPath(path, method);

		if (isAdminPath && !"ADMIN".equals(role)) {
			exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
			return exchange.getResponse().setComplete();
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1;
	}
}
