package com.microservices.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	// Gateway Route Configuration
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()

				// ----------------- service-a -----------------
				.route(r -> r.path("/service-a/**")
						.filters(f -> f
								.stripPrefix(1)
								.addResponseHeader("X-Response-Header", "ApiGateway"))
						.uri("lb://service-a"))

				// ----------------- service-b -----------------
				.route(r -> r.path("/service-b/**")
						.filters(f -> f
								.stripPrefix(1)
								.addResponseHeader("X-Response-Header", "ApiGateway"))
						.uri("lb://service-b"))

				.build();


	}

}
