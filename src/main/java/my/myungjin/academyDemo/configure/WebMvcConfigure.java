package my.myungjin.academyDemo.configure;


import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.configure.support.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfigure implements WebMvcConfigurer {
//    private final String baseApiPath = "api";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.css", "/**/*.html", "/**/*.js", "/**/*.png", "/**/*.mustache")
                .setCachePeriod(0)
                .addResourceLocations("classpath:/templates/");

        registry.addResourceHandler("/")
                .setCachePeriod(0)
                .addResourceLocations("classpath:/templates/index.mustache");
                /*.resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) {
                        if (resourcePath.startsWith(baseApiPath) || resourcePath.startsWith(baseApiPath.substring(1))) {
                            return null;
                        }
                        return location.exists() && location.isReadable() ? location : null;
                    }
                });*/

    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor(){
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/")
                .addPathPatterns("/login")
                .addPathPatterns("/order/*")
                .addPathPatterns("/myPage")
                .addPathPatterns("/member/me")
                .addPathPatterns("/admin/**")
        ;
    }
}
