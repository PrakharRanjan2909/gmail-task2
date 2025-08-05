package com.example.demo_task2.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Prevent HTML escaping in JSON output
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);

        return mapper;
    }
}
