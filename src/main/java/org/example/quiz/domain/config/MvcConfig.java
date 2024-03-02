package org.example.quiz.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    private final String uploadDirectory = System.getProperty("user.dir") + "\\data\\images";
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println(uploadDirectory);
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("file:" + uploadDirectory + "\\");
    }
}
