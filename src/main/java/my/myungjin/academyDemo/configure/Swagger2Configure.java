package my.myungjin.academyDemo.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;

import static java.util.Collections.singletonList;

@Configuration
@EnableSwagger2
public class Swagger2Configure implements WebMvcConfigurer {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("example")
                .securitySchemes(singletonList(apiKey()))
                .directModelSubstitute(LocalDate.class, String.class)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("my.myungjin.academyDemo.web.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(this.apiInfo())
                .enable(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Demo")
                .description("API EXAMPLE")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Cookie", "header");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
