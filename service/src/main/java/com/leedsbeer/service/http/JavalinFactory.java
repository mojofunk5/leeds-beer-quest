package com.leedsbeer.service.http;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

public class JavalinFactory {

    private JavalinFactory() {
        super();
    }

    public static Javalin create() {
        return Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(ObjectMapperFactory.create()));
            config.registerPlugin(new OpenApiPlugin(createOpenApiOptions()));
        });
    }

    private static OpenApiOptions createOpenApiOptions() {
        Package thisPackage = JavalinFactory.class.getPackage();
        Info info = new Info()
                .title(thisPackage.getImplementationTitle())
                .version(thisPackage.getImplementationVersion());
        return new OpenApiOptions(info)
                .activateAnnotationScanningFor(thisPackage.getName())
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger-ui"))
                .jacksonMapper(ObjectMapperFactory.createForSwagger());
    }
}