package dev.emrygun.forecast.client.openweather;

import dev.emrygun.forecast.dto.client.openweather.OpenWeatherForecastResponse;
import dev.emrygun.forecast.model.CityName;

public interface OpenWeatherForecastClient {

    OpenWeatherForecastResponse fetchWeatherForecastOfNext4Days(CityName city);
}
