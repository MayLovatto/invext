package com.invext.requestdistributor.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Invext Request Distributor API")
                        .version("1.0.0")
                        .description("API documentation for the Invext Request Distributor application.")
                        .contact(new Contact()
                                .name("Maysa Lopes")
                                .email("maysa.lopes@example.com")
                                .url("https://www.invext.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                );
    }
}
