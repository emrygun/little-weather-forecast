package dev.emrygun.forecast.mapper;

import dev.emrygun.forecast.dto.WeatherForecast;
import dev.emrygun.forecast.dto.client.openweather.OpenWeatherForecastResponse;
import dev.emrygun.forecast.dto.client.openweather.WeatherForecastInformationWrapper;
import dev.emrygun.forecast.dto.WeatherForecastReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
            CityNameMapper.class
        }
)
public interface OpenWeatherResponseMapper {

    @Mapping(source = "cityInformation.name", target = "city")
    @Mapping(source = "weatherInformation", target = "weatherForecasts")
    WeatherForecastReport toWeatherForecastReport(OpenWeatherForecastResponse response);

    @Mapping(source = "timestamp", target = "dateTime")
    @Mapping(source = "weatherForecastInformation.maximum", target = "maximumTemp")
    @Mapping(source = "weatherForecastInformation.feelsLike", target = "feelsLikeTemp")
    @Mapping(source = "weatherForecastInformation.humidity", target = "humidity")
    WeatherForecast toWeatherForecast(WeatherForecastInformationWrapper informationWrapper);

    List<WeatherForecast> toWeatherForecasts(List<WeatherForecastInformationWrapper> informationWrappers);
}
