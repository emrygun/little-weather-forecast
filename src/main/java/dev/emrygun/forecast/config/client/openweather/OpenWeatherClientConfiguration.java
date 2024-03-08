package dev.emrygun.forecast.config.client.openweather;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class OpenWeatherClientConfiguration {

    public static final String OPENWEATHER_API_KEY_REQUEST_PARAM = "APPID";

    private final OpenWeatherClientConfigurationProperties clientConfigurationProperties;

    public OpenWeatherClientConfiguration(OpenWeatherClientConfigurationProperties clientConfigurationProperties) {
        this.clientConfigurationProperties = clientConfigurationProperties;
    }

    @Bean
    public RestClient openWeatherRestClient() {
        var uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(clientConfigurationProperties.getBaseUrl())
                .queryParam(OPENWEATHER_API_KEY_REQUEST_PARAM, clientConfigurationProperties.getApiKey());
        var uriBuilderFactory = new DefaultUriBuilderFactory(uriComponentsBuilder);

        return RestClient.builder()
                .uriBuilderFactory(uriBuilderFactory)
                .defaultStatusHandler(new OpenWeatherClientExceptionHandler())
                .build();
    }

}
