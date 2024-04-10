package com.dedogames.sumary.api.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI canalhaApiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Summary")
                                .description("API responsible for retrive summary")
                                .version("v1")
                                .license(
                                        new License()
                                                .name("Proprietary License")
                                                .url("http://ggrcursos.com"))
                                .contact(new Contact().name("Summary-Api").email("geoldery@gmail.com")));
    }
}