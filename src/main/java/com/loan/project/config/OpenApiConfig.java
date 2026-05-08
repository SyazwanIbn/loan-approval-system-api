package com.loan.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanSystemOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistem Kelulusan Pinjaman Api")
                        .description("API untuk permohonan dan kelulusan pinjaman, mematuhi spesifikasi DSR dan standard perbankan (FSA 2013).")
                        .version("v1.0")
                        .contact(new Contact()
                        .name("Muhammad Nursyazwan") // Letak nama kau sebagai author
                        .email("28nursyazwan@gmail.com"))); // Tukar e-mel kau

    }
}
