package org.example.expert.global.config;

import lombok.RequiredArgsConstructor;
import org.example.expert.global.interceptor.AdminInterceptor;
import org.example.expert.global.resolver.AuthUserArgumentResolver;
import org.example.expert.global.security.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;

    // ArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtUtil))
            .order(1)
            .addPathPatterns("/admin/**");
    }
}
