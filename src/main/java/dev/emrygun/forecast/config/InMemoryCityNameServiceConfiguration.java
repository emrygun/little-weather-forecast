package dev.emrygun.forecast.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "little-weather-forecast.service.city-name-service.in-memory")
public class InMemoryCityNameServiceConfiguration {
    private String cityListResourcePath = "classpath:city.list.json";

    public InMemoryCityNameServiceConfiguration() {
    }

    public String getCityListResourcePath() {
        return cityListResourcePath;
    }

    public void setCityListResourcePath(String cityListResourcePath) {
        this.cityListResourcePath = cityListResourcePath;
    }
}
