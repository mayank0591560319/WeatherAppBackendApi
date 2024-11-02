package com.mayank.advanced_weather_api.dao;
import lombok.Data;

@Data
public class CurrentWeatherFields {
String city;
String state;
String  country;
Double  windSpeed;
Double pressure;
Double precipitation;
Double humidity;
Integer cloud;
Double windchill;
Double uv;
Double heat;
Double feelsLike;


}
