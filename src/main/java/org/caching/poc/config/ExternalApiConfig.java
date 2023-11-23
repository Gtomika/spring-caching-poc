package org.caching.poc.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Profile("api-backend")
@Configuration
@Getter
public class ExternalApiConfig {

    private final String externalApiUrl;
    private final String externalApiPath;

    public ExternalApiConfig(
            @Value("${external-api.url}") String externalApiUrl,
            @Value("${external-api.path}") String externalApiPath
    ) {
        this.externalApiUrl = externalApiUrl;
        this.externalApiPath = externalApiPath;

        log.info("The external API configurations will use the URL '{}' with path '{}'", externalApiUrl, externalApiPath);
    }

    @Bean
    public WebClient externalApiClient() {
        return WebClient.builder()
                .baseUrl(externalApiUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "spring-caching-poc-application")
                .build();
    }
}
