package kr.go.smplatform.itsm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.go.smplatform.itsm.intcpt.ApiAccessInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final ApiAccessInterceptor apiAccessInterceptor;

    public WebMvcConfig(ApiAccessInterceptor apiAccessInterceptor) {
        this.apiAccessInterceptor = apiAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAccessInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/v1/auth/**", "/api/v1/hist/**", "/actuator/**", "/error");
    }
}
