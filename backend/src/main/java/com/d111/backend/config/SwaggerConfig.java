package com.d111.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "StyleFinder API 명세서",
                description = "StyleFinder API 명세서",
                version = "2.0"
        )
)
@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI openAPI() {
                return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("JWT"))
                        .components(new Components().addSecuritySchemes("JWT", createAPIKeyScheme()));
        }

        @Bean
        public GroupedOpenApi sample() {
                return GroupedOpenApi.builder()
                        .group("sample")
                        .pathsToMatch("/api/sample/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi user() {
                return GroupedOpenApi.builder()
                        .group("user")
                        .pathsToMatch("/api/user/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi feed() {
                return GroupedOpenApi.builder()
                        .group("feed")
                        .pathsToMatch("/api/feed/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi coordi() {
                return GroupedOpenApi.builder()
                        .group("coordi")
                        .pathsToMatch("/api/coordi/**")
                        .build();
        }

        @Bean
        public GroupedOpenApi comment() {
                return GroupedOpenApi.builder()
                        .group("comment")
                        .pathsToMatch("/api/comment/**")
                        .build();
        }

        private SecurityScheme createAPIKeyScheme() {
                return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer");
        }

}
