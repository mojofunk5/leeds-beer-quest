package com.leedsbeer.service.http;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

public class JavalinFactory {

    private JavalinFactory() {
        super();
    }

    public static Javalin create() {
        return Javalin.create(config -> config.jsonMapper(new JavalinJackson(ObjectMapperFactory.create())));
    }
}