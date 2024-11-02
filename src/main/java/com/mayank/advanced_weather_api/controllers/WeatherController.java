package com.mayank.advanced_weather_api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.mayank.advanced_weather_api.constants.AppConstant;
import com.mayank.advanced_weather_api.exception.BadRequestException;
import com.mayank.advanced_weather_api.exception.InternalServerError;
import com.mayank.advanced_weather_api.exception.UnAuthorizedException;
import com.mayank.advanced_weather_api.services.WeatherServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WeatherController {

    @Autowired
    WeatherServices weatherServices;

    @GetMapping("/weather_app")
    public Object getWeatherDetails(@RequestParam(value = "location", required = false) String location) {
        Map<String, Object> results = new LinkedHashMap<>();
        List<JsonNode> weatherDetails = new ArrayList<>();
        try {
            if (location.contains(",")) {
                try {
                    Double lat = Double.parseDouble(location.split(",")[0]);
                    Double lng = Double.parseDouble(location.split(",")[1]);
                    weatherDetails = weatherServices.weatherApp(lat, lng);
                    return weatherDetails;
                } catch (NumberFormatException e) {
                    throw new BadRequestException(AppConstant.BadRequestExceptionMessage);
                }
            }
        } catch (BadRequestException e) {
            results.put("message", AppConstant.BadRequestExceptionMessage);
            results.put("responseCode", HttpStatus.BAD_REQUEST);
            return new ResponseEntity(results, HttpStatus.BAD_REQUEST);
        } catch (UnAuthorizedException e) {
            results.put("message", AppConstant.UnAuthorizedExceptionMessage);
            results.put("responseCode", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity(results, HttpStatus.UNAUTHORIZED);
        } catch (InternalServerError e) {
            results.put("message", AppConstant.InternalServerErrorMessage);
            results.put("responseCode", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity(results, HttpStatus.INTERNAL_SERVER_ERROR);
        }
      return weatherDetails;
    }

}
