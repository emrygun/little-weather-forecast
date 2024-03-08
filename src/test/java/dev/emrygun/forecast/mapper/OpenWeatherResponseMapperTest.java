package dev.emrygun.forecast.mapper;

import dev.emrygun.forecast.model.CityName;
import dev.emrygun.forecast.dto.WeatherForecast;
import dev.emrygun.forecast.dto.WeatherForecastReport;
import dev.emrygun.forecast.dto.client.openweather.CityInformation;
import dev.emrygun.forecast.dto.client.openweather.OpenWeatherForecastResponse;
import dev.emrygun.forecast.dto.client.openweather.WeatherForecastInformation;
import dev.emrygun.forecast.dto.client.openweather.WeatherForecastInformationWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {
        CityNameMapperImpl.class,
        OpenWeatherResponseMapperImpl.class
})
@ExtendWith(SpringExtension.class)
class OpenWeatherResponseMapperTest {

    @Autowired
    private OpenWeatherResponseMapper openWeatherResponseMapper;

    @Test
    @DisplayName("Should map OpenWeatherForecastResponse into WeatherForecastReport")
    void shouldMapOpenWeatherForecastResponseIntoWeatherForecastReport() {
        var now = Instant.now();

        // Create OpenWeather response
        var cityInformation = new CityInformation("Ankara", "TR");
        var weatherInformationWrapper = List.of(
                new WeatherForecastInformationWrapper(now, new WeatherForecastInformation(1F, 1F, 1F))
        );
        var forecastResponse = new OpenWeatherForecastResponse(cityInformation, weatherInformationWrapper);

        // Create WeatherForecastReport
        var weatherForecastReport = new WeatherForecastReport(
                new CityName("Ankara"),
                List.of(new WeatherForecast(now, 1F, 1F, 1F))
        );

        assertEquals(weatherForecastReport, openWeatherResponseMapper.toWeatherForecastReport(forecastResponse));
    }

    @Test
    @DisplayName("Should map WeatherForecastInformationWrapper into WeatherForecast")
    void shouldMapWeatherForecastInformationWrapperIntoWeatherForecast() {
        var now = Instant.now();

        var weatherInformationWrapper = new WeatherForecastInformationWrapper(now, new WeatherForecastInformation(1F, 1F, 1F));
        var weatherForecast = new WeatherForecast(now, 1F, 1F, 1F);

        assertEquals(weatherForecast, openWeatherResponseMapper.toWeatherForecast(weatherInformationWrapper));
    }

    @Test
    @DisplayName("Should map WeatherForecastInformationWrapper list into WeatherForecast list")
    void toWeatherForecasts() {
        var now = Instant.now();

        var weatherInformationWrapper = List.of(new WeatherForecastInformationWrapper(now, new WeatherForecastInformation(1F, 1F, 1F)));
        var weatherForecast = List.of(new WeatherForecast(now, 1F, 1F, 1F));

        assertEquals(weatherForecast, openWeatherResponseMapper.toWeatherForecasts(weatherInformationWrapper));
    }
}