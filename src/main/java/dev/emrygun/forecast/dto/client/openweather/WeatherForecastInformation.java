package dev.emrygun.forecast.dto.client.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherForecastInformation(
        @JsonProperty("temp_max") Float maximum,
        @JsonProperty("feels_like") Float feelsLike,
        @JsonProperty("humidity") Float humidity
) {
}
