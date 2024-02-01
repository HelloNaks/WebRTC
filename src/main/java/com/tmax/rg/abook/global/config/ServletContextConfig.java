package com.tmax.rg.abook.global.config;

import com.tmax.rg.abook.global.interceptor.ApiInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ServletContextConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { // 인터셉터 등록
        registry.addInterceptor(apiInterceptor())
                .addPathPatterns("/**") // Interceptor가 적용될 경로
                .excludePathPatterns("/v3/api-docs")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/csrf")
                .excludePathPatterns("/error**"); // Interceptor가 적용되지 않을 경로
    }

    @Bean
    public ApiInterceptor apiInterceptor() {
        return new ApiInterceptor();
    }
}