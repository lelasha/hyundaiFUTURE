package com.web.hyundai.config;

import com.google.api.client.util.Lists;
import com.web.hyundai.model.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;

import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.web.Swagger2Controller;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(Path.folderPath());
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + Path.folderPath() )
                .addResourceLocations("classpath:/static/");

        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE");
    }
// swagger


@Bean
public Docket swaggerApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            //.paths(PathSelectors.regex("\\/api\\/.*?"))
            .paths(PathSelectors.any())
            .build()
            //.host("api.myborbali.ge")
            .apiInfo(swaggerApiInfo())
            .useDefaultResponseMessages(false)
            .securityContexts(Collections.singletonList(securityContext()))
            .securitySchemes(Collections.singletonList(apiKey()));
}

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/admin/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("JWT", authorizationScopes));
    }


    @Bean
    ApiInfo swaggerApiInfo() {
        final ApiInfoBuilder builder = new ApiInfoBuilder();
        return builder.title("Hyundai api/admin").version("0.1").build();
    }

    @Bean
    UiConfiguration swaggerUiConfiguration() {
        return UiConfigurationBuilder.builder()
                .defaultModelRendering(ModelRendering.MODEL)
                .defaultModelsExpandDepth(1)
                .docExpansion(DocExpansion.LIST)
                .build();
    }

}
