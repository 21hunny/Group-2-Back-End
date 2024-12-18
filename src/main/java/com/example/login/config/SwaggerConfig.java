package com.example.login.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(url = "https://virtserver.swaggerhub.com/LASINDUTHEMIYA96/EAD2/1.0.0", description = "SwaggerHub Virtual Server"),
                @Server(url = "http://localhost:8081/swagger-ui", description = "Local Development Server")
        }
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Login API Documentation")
                        .version("1.0.0")
                        .description("API documentation for login system with Admin, Student, and Lecturer roles."));
    }
}
