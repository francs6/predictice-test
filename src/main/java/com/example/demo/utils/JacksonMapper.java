package com.example.demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.TimeZone;

/**
 * Utils class for json operations
 */
public class JacksonMapper {
    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(JacksonMapper.class);

    /**
     * The Constant GMT_TIMEZONE.
     */
    private static final String GMT_TIMEZONE = "GMT";

    private static ObjectMapper defaultJacksonMapper = getSimpleJacksonMapper();

    private JacksonMapper() {

    }

    /**
     * Return a simple jackson object mapper
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getSimpleJacksonMapper() {
        ObjectMapper mapper = new Jackson2ObjectMapperBuilder().build();
        configureMapper(mapper);
        return mapper;
    }

    /**
     * Configure mapper according to andie needs
     *
     * @param mapper to configure
     */
    public static void configureMapper(ObjectMapper mapper) {

        mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        mapper.configure(Feature.IGNORE_UNDEFINED, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // StdDateFormat is ISO8601 since jackson 2.9
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        mapper.setTimeZone(TimeZone.getTimeZone(GMT_TIMEZONE));
        mapper.registerModule(new ParameterNamesModule())
                // .registerModule(zonedDateTimeModule)
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new SpringDataJacksonConfiguration.PageModule());
                //.registerModule(new SortModule());

        mapper.configure(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID, true);
    }

    public static ObjectMapper getDefaultJacksonMapper() {
        if (defaultJacksonMapper == null) {
            defaultJacksonMapper = getSimpleJacksonMapper();
        }
        return defaultJacksonMapper;
    }

    /**
     * Serialize item passed in param to json string
     *
     * @param in : item to serialize
     * @return json string
     */
    public static String toJsonString(Object in) {
        try {
            return getDefaultJacksonMapper().writeValueAsString(in);
        } catch (JsonProcessingException e) {
            LOG.error("Cannot serialize item with type {}", in.getClass().getSimpleName(), e);
            return null;
        }
    }

    public static String toJsonString(Object in, Class<?> view) {
        try {
            return getDefaultJacksonMapper().writerWithView(view).writeValueAsString(in);
        } catch (JsonProcessingException e) {
            LOG.error("Cannot serialize item", e);
            return null;
        }
    }

    /**
     * Built an item T from json String
     *
     * @param in : json String to parse
     * @param clazz : class destination
     * @param <T> : type destination
     * @return T
     */
    public static <T> T fromJsonString(String in, Class<T> clazz) {
        try {
            return getDefaultJacksonMapper().readValue(in.getBytes(), clazz);
        } catch (IOException e) {
            LOG.error("Cannot build item {}:\n {}", in, e.getMessage());
            return null;
        }
    }

    public static <T> T fromJsonString(String in, TypeReference<T> typeReference) {
        try {
            return getDefaultJacksonMapper().readValue(in.getBytes(), typeReference);
        } catch (IOException e) {
            LOG.error("Cannot build item", e);
            return null;
        }
    }

}
