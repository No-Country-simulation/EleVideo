package com.elevideo.backend.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class EmailConfig {

    @Value("${resend.base-url:https://api.resend.com}")
    private String resendBaseUrl;

    @Bean
    public RestClient resendRestClient() {
        return RestClient.builder()
                .baseUrl(resendBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
