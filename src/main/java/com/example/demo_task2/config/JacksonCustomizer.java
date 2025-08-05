package com.example.demo_task2.config;

import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCustomizer {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.postConfigurer(objectMapper -> {
            objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, false);
        });
    }
}
