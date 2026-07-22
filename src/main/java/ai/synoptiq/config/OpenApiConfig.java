package ai.synoptiq.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI synoptiqOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()
                                .title("Synoptiq AI API")
                                .version("1.0.0")
                                .description("""
                                        Synoptiq AI is an AI-powered workflow automation platform
                                        inspired by Magai.

                                        This API provides integrations for AI chat, Gmail,
                                        workflow automation, email intelligence, and future
                                        integrations such as Notion, Slack, GitHub,
                                        LinkedIn, YouTube, Shopify, and more.
                                        """)
                                .license(
                                        new License()
                                                .name("Private")
                                )
                )

                .externalDocs(
                        new ExternalDocumentation()
                                .description("Synoptiq Documentation")
                                .url("https://github.com/abhiramaab/synoptiq")
                );

    }

}