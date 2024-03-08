package dev.emrygun.forecast.dto.client.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OpenWeatherForecastResponse (
        @JsonProperty("city") CityInformation cityInformation,
        @JsonProperty("list") List<WeatherForecastInformationWrapper> weatherInformation

) { }
