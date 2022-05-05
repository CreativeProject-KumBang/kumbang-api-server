package com.se.kumbangapiserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${external.static.url.outbound}")
    private String connectPath;

    @Value("${external.static.url.inbound}")
    private String resourcePath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String newResourcePath = "file://" + resourcePath;
        registry.addResourceHandler(connectPath).addResourceLocations(newResourcePath);
    }
}
