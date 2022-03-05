package com.leedsbeer.service.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import io.swagger.v3.core.jackson.mixin.SchemaMixin;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;

public class ObjectMapperFactory {

    private ObjectMapperFactory() {
        super();
    }

    public static ObjectMapper create() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    public static ObjectMapper createForSwagger() {
        ToStringSerializer toStringSerializer = new ToStringSerializer();
        return ExtensionsKt.jacksonObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new SimpleModule()
                        .addSerializer(SecurityScheme.In.class, toStringSerializer)
                        .addSerializer(SecurityScheme.Type.class, toStringSerializer))
                .addMixIn(Schema.class, SchemaMixin.class)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}