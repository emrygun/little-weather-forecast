package dev.emrygun.forecast.dto.client.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record WeatherForecastInformationWrapper(
        @JsonProperty("dt") Instant timestamp,
        @JsonProperty("main") WeatherForecastInformation weatherForecastInformation
) { }
