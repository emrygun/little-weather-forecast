package dev.emrygun.forecast.dto;

import dev.emrygun.forecast.model.CityName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record WeatherForecastReport(
        @Schema(title = "City Name")
        CityName city,
        @Schema(title = "Weather Forecasts")
        List<WeatherForecast> weatherForecasts
) { }
