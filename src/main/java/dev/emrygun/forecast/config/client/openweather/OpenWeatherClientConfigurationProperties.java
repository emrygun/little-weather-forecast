package dev.emrygun.forecast.config.client.openweather;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "little-weather-forecast.client.open-weather")
public class OpenWeatherClientConfigurationProperties {

    @NotEmpty
    @URL
    private String baseUrl;

    @NotEmpty
    private  String apiKey;

    public OpenWeatherClientConfigurationProperties() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
