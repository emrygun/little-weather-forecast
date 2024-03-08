package dev.emrygun.forecast.service;

import dev.emrygun.forecast.dto.client.openweather.WeatherForecastInformationWrapper;
import dev.emrygun.forecast.exception.CityNotFoundException;
import dev.emrygun.forecast.client.openweather.OpenWeatherForecastClient;
import dev.emrygun.forecast.config.cache.CacheConfiguration;
import dev.emrygun.forecast.model.CityName;
import dev.emrygun.forecast.dto.WeatherForecastReport;
import dev.emrygun.forecast.mapper.OpenWeatherResponseMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Service
public class WeatherForecastService {
    private final OpenWeatherForecastClient forecastClient;
    private final OpenWeatherResponseMapper openWeatherResponseMapper;
    private final CityNameService cityNameService;

    public WeatherForecastService(OpenWeatherForecastClient forecastClient,
                                  OpenWeatherResponseMapper openWeatherResponseMapper,
                                  CityNameService cityNameService) {
        this.forecastClient = forecastClient;
        this.openWeatherResponseMapper = openWeatherResponseMapper;
        this.cityNameService = cityNameService;
    }

    @Cacheable(value = CacheConfiguration.WEATHER_FORECAST, key = "{#root.methodName : #cityName.value}")
    public WeatherForecastReport getWeatherForecastOfNext48Hours(CityName cityName) {
        if (!cityNameService.isCityNameExists(cityName)) {
            throw new CityNotFoundException(cityName);
        }

        var openWeatherResponse = forecastClient.fetchWeatherForecastOfNext4Days(cityName);

        // Filter information of next 48 hours.
        var maxTime = Instant.now().plus(2, ChronoUnit.DAYS);
        var next48HoursWeatherResponses = openWeatherResponse.weatherInformation().stream()
                .filter(info -> info.timestamp().isBefore(maxTime))
                .sorted(Comparator.comparing(WeatherForecastInformationWrapper::timestamp))
                .map(openWeatherResponseMapper::toWeatherForecast)
                .toList();

        return new WeatherForecastReport(
                new CityName(openWeatherResponse.cityInformation().name()),
                next48HoursWeatherResponses
        );
    }
}
