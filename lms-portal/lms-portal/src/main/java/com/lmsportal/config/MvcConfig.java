package com.lmsportal.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
        Path brandUploadDir=Paths.get("./user-photos");
        String brandUploadPath=brandUploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/user-photos/**").addResourceLocations("file:/"+brandUploadPath+"/");
    } 
}
