package project3.nutrisubscriptionservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


//@OpenAPIDefinition(
//        info = @Info(title = "Nutri-subcscribe API 명세서",
//                description = "영양제 구독서비스 API 명세서",
//                version = "v1"))
@Configuration
@Component
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("영양제 구독서비스 API")
                .description("영양제 구독서비스 API 목록입니다.");

        return new OpenAPI()
                .info(info);
    }
}
