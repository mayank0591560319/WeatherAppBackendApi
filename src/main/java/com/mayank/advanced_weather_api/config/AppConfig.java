package com.mayank.advanced_weather_api.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayank.advanced_weather_api.dao.CurrentWeatherFields;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient();
    }

    @Bean
    public ObjectMapper objectMapper(){

        return new ObjectMapper();
    }

    @Bean
    public CurrentWeatherFields currentWeatherFields(){
        return new CurrentWeatherFields();
    }


}
