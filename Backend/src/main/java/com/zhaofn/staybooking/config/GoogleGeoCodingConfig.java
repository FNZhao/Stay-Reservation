package com.zhaofn.staybooking.config;


import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GoogleGeoCodingConfig {


    @Value("${geocoding.apikey}")
    private String apiKey;


    @Bean
    public GeoApiContext geoApiContext() {//有这个context就可以去操作
        return new GeoApiContext.Builder().apiKey(apiKey).build();
    }
}
