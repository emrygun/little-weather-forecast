package dev.emrygun.forecast.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record WeatherForecast(
        @Schema(title = "Date Time", description = "Date time of weather forecast in UTC timezone.")
        Instant dateTime,
        @Schema(title = "Maximum Temperature")
        Float maximumTemp,
        @Schema(title = "Feels Like Temperature")
        Float feelsLikeTemp,
        @Schema(title = "Humidity")
        Float humidity
) { }
