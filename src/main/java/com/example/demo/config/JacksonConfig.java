package com.example.demo.config;

import com.example.demo.utils.JacksonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    /**
     * Required to map properly ES {@link org.springframework.data.elasticsearch.core.SearchHits} to JSON
     * @return a jackson mapper configured
     */
    @Bean
    public ObjectMapper jsonObjectMapper() {
        return JacksonMapper.getSimpleJacksonMapper();
    }
}
