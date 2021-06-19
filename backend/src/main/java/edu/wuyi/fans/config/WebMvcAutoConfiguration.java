package edu.wuyi.fans.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/16
 */

@Configuration
public class WebMvcAutoConfiguration extends WebMvcConfigurationSupport {
    @Value("${fans.config.upload-path}")
    private String UPLOAD_PATH;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/picture/**")
                .addResourceLocations("file:"+UPLOAD_PATH+"picture/");

        registry.addResourceHandler("/profile-picture/**")
                .addResourceLocations("file:"+UPLOAD_PATH+"profile-picture/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
