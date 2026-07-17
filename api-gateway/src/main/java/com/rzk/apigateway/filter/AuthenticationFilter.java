package com.rzk.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthenticationFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
//    private static final String HEADER_NAME = "x-rental-api-key";
//
//    @Value("${gateway.auth.key:rental-access-key}")
//    private String expectedKey;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String apiKey = exchange.getRequest().getHeaders().getFirst(HEADER_NAME);
//
//        if (apiKey == null || !apiKey.equals(expectedKey)) {
//            logger.warn("Unauthorized access attempt with key: {}", apiKey);
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange);
//    }

    // Lista ruta koje su potpuno javne i ne zahtevaju login
    private final List<String> publicEndpoints = List.of(
            "/api/users/login",
            "/home",
            "/vehicles/available"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        if (path.startsWith("/uploads/vehicles/")) {
            return chain.filter(exchange);
        }
        if (publicEndpoints.contains(path) || (path.equals("/api/users") && method.equals("POST"))) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(org.springframework.http.HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Fali ili je neispravan Authorization header za putanju: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7); // Preskačemo tekst "Bearer " da dobijemo čist token

        try {
            java.security.Key dynamicKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                    "mojUltraTajniKljucKojiMoraBitiJakoDugacak1234567890".getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );

            Claims claims = Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) dynamicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String roles = claims.get("roles", String.class);
            exchange = exchange.mutate()
                    .request(exchange.getRequest().mutate().header("X-User-Roles", roles).build())
                    .build();

            logger.info("Uspešna autentifikacija za korisnika: {}, uloge: {}", claims.getSubject(), roles);

        } catch (Exception e) {
            logger.error("Validacija tokena neuspešna: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}