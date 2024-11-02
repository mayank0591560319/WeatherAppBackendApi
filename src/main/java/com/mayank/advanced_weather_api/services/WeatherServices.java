package com.mayank.advanced_weather_api.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mayank.advanced_weather_api.config.PropertyConfig;
import com.mayank.advanced_weather_api.dao.CurrentWeatherFields;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class WeatherServices {
    @Autowired
    PropertyConfig propertyConfig;

    @Autowired
    OkHttpClient okHttpClient;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CurrentWeatherFields currentWeatherFields;

    public List<JsonNode> weatherApp(Double lat, Double lng) {
        Response response = null;
        List<JsonNode> weatherResult = new ArrayList<>();
        try {
            response = requestProcessor(lat, lng);
            if (response != null && response.isSuccessful()) {
                String body = response.body().string();
                JsonNode rootNode = objectMapper.readTree(body);
                JsonNode locationNode = rootNode.path("location");
                if (locationNode != null && !locationNode.isEmpty()) {
                    currentWeatherFields.setCity(locationNode.get("name").toString().replaceAll("\"",""));
                    currentWeatherFields.setState(locationNode.get("region").toString().replaceAll("\"",""));
                    currentWeatherFields.setCountry(locationNode.get("country").toString().replaceAll("\"",""));
                }
                JsonNode currentNode = rootNode.path("current");
                if (currentNode != null && !currentNode.isEmpty()) {
                    currentWeatherFields.setWindSpeed(Double.parseDouble(currentNode.get("wind_mph").toString()));
                    currentWeatherFields.setCloud(Integer.parseInt(currentNode.get("cloud").toString()));
                    currentWeatherFields.setHumidity(Double.parseDouble(currentNode.get("humidity").toString()));
                    currentWeatherFields.setPrecipitation(Double.parseDouble(currentNode.get("precip_mm").toString()));
                    currentWeatherFields.setHeat(Double.parseDouble(currentNode.get("heatindex_c").toString()));
                    currentWeatherFields.setPressure(Double.parseDouble(currentNode.get("pressure_mb").toString()));
                    currentWeatherFields.setWindchill(Double.parseDouble(currentNode.get("windchill_c").toString()));
                    currentWeatherFields.setFeelsLike(Double.parseDouble(currentNode.get("feelslike_c").toString()));
                    currentWeatherFields.setUv(Double.parseDouble(currentNode.get("uv").toString()));
                }
                JsonNode result = objectMapper.convertValue(currentWeatherFields, new TypeReference<JsonNode>() {
                });
                weatherResult.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return weatherResult;
    }

    public Response requestProcessor(Double lat, Double lng) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .url(createUrl(lat + "," + lng))
                    .build();
            response = okHttpClient.newCall(request).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String createUrl(String location) {
        StringBuilder url = new StringBuilder(propertyConfig.getWeatherApiUrl());
        return url.append(propertyConfig.accessToken).append("&").append("q").append("=").append(location).toString();

    }


}
