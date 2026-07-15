//package com.rzk.apigateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator coffeeRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("home", r -> r
//                        .path("/home")
//                        .filters(f -> f.rewritePath("/home", "/api/users")
//                                .addRequestHeader("x-rental-api-key", "rental-access-key"))
//                        .uri("lb://user-service"))
//                .route("billing-service", r -> r.path("/bills/**")
//                        .uri("lb://billing-service"))
//
//                .route("rental-service", r -> r.path("/rentals/**")
//                        .uri("lb://rental-service"))
//
//                .route("user-service", r -> r.path("/api/users/**")
//                        .uri("lb://user-service"))
//
//                .route("vehicle-service", r -> r.path("/vehicles/**")
//                        .uri("lb://vehicle-service"))
//                .build();
//    }
//}
package com.rzk.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator coffeeRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("home", r -> r
                        .path("/home")
                        .filters(f -> f.rewritePath("/home", "/api/users")) // IZBAČEN .addRequestHeader
                        .uri("lb://user-service"))

                .route("billing-service", r -> r.path("/bills/**")
                        .uri("lb://billing-service"))

                .route("rental-service", r -> r.path("/rentals/**")
                        .uri("lb://rental-service"))

                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))

                .route("vehicle-service", r -> r.path("/vehicles/**")
                        .uri("lb://vehicle-service"))
                .build();
    }
}