package com.mayank.advanced_weather_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class PropertyConfig {

    @Value("${weather.api.url}")
    public String weatherApiUrl;

    @Value("${access.token}")
    public String accessToken;
}
