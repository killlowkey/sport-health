package com.xzc.sport.health.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ray
 * @date created in 2020/8/31 13:26
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 是否发送cookie信息
        config.addAllowedOrigin("http://localhost:8080"); // 允许域名
        config.addAllowedHeader("*"); // 允许任何头
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 允许任何方法

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }


    static class LocalDateTimeConverter implements Converter<String[], List> {

        @Override
        public List convert(String[] source) {
            List<LocalDateTime> times = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Arrays.stream(source).forEach(str -> times.add(LocalDateTime.parse(str, formatter)));
            return times;
        }
    }

    @Bean
    public Converter localDateTimeConverter() {
        return new LocalDateTimeConverter();
    }
}