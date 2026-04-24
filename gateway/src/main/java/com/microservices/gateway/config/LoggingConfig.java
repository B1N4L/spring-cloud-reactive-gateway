package com.microservices.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;

@Configuration
public class LoggingConfig {

    private static final Logger log = LoggerFactory.getLogger(LoggingConfig.class);

    @Bean
    public GlobalFilter requestLoggingFilter() {
        return (exchange, chain) -> {
            String method = exchange.getRequest().getMethod().name();
            String path = exchange.getRequest().getURI().getPath();
            Instant start = Instant.now();

            log.info("Incoming request: {} {}", method, path);

            return chain.filter(exchange)
                    .doFinally(signalType -> {
                        Duration duration = Duration.between(start, Instant.now());
                        int status = exchange.getResponse().getStatusCode() != null
                                ? exchange.getResponse().getStatusCode().value()
                                : 200;

                        log.info("Completed request: {} {} -> {} ({} ms)",
                                method, path, status, duration.toMillis());
                    });
        };
    }
}