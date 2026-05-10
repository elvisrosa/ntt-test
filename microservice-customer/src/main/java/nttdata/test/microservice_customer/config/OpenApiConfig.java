package nttdata.test.microservice_customer.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig implements WebFluxConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservice Customer API")
                        .version("1.0.0")
                        .description("OpenAPI contract (Contract-First) for the Customer microservice")
                        .contact(new Contact()
                                .name("NTT Data Test Team")
                                .email("test@nttdata.com")));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir archivos estáticos desde /static
        registry.addResourceHandler("/openapi/**")
                .addResourceLocations("classpath:/static/openapi/");
    }
}