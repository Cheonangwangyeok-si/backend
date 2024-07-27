package com.likelion.mindiary.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {

    @Value("${openai.secret-key}")
    private String secretKey;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.set("Authorization",
                    "Bearer " + secretKey); // Ensure there is a space after "Bearer"
            headers.setContentType(MediaType.APPLICATION_JSON); // Set content type if needed
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
