package br.com.henrique.JWT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Minha API")
                        .version("v1")
                        .description("Treinando java")
                        .termsOfService("https://henriquefrancoa.github.io/portifolio/")
                        .license(new License().name("Apache 2.0")
                                .url("https://henriquefrancoa.github.io/portifolio/")));

    }
}
