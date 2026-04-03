package com.example.votacao.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Voting API")
                        .version("v1")
                        .description("API para cadastro de agendas (assembléias), abertura de sessões, votação e apuração. " +
                                "A estratégia de versionamento principal é por URI (/api/v1) e, opcionalmente, " +
                                "o cliente pode enviar o header X-API-Version=1 ou v1 para compatibilidade explícita.")
                        .contact(new Contact().name("Exemplo técnico")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addParameters("ApiVersionHeader", new Parameter()
                                .in("header")
                                .name("X-API-Version")
                                .description("Versão da API. Opcional para a v1, aceita 1 ou v1.")
                                .required(false))
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }
}
