package com.tmax.rg.abook.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;


@Configuration
@Profile("!prod")
public class SwaggerConfig {
    private static final String TOKEN_KEY = "ACCESS-TOKEN";
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(TOKEN_KEY, apiKeySecuritySchema()))
                .info(new Info().title("Member A-Book Backend API")
                        .description("Tmax RG Member A-Book Backend Application API doc")
                        .contact(new Contact().name("Tmax"))
                        .license(new License().name("Tmax RG")))
                .security(Collections.singletonList(new SecurityRequirement().addList(TOKEN_KEY)));
    }

    public SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name(TOKEN_KEY)
                .description("로그인을 통해 받은 ACCESS-TOKEN 값을 설정하세요.")
                .in(SecurityScheme.In.COOKIE)
                .type(SecurityScheme.Type.APIKEY);
    }

    @Bean
    public GroupedOpenApi apiV1() {
        return GroupedOpenApi.builder()
                .group("api-version-1")
                .pathsToMatch("/**")
                .pathsToExclude("/test/**")
                .build();
    }
}
