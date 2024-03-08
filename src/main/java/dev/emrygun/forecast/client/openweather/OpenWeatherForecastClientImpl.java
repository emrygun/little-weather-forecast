package dev.emrygun.forecast.client.openweather;

import dev.emrygun.forecast.dto.client.openweather.OpenWeatherForecastResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import dev.emrygun.forecast.model.CityName;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class OpenWeatherForecastClientImpl implements OpenWeatherForecastClient {
    private final RestClient openWeatherRestClient;
    private final CircuitBreaker openWeatherCircuitBreaker;

    public OpenWeatherForecastClientImpl(RestClient openWeatherRestClient,
                                         CircuitBreaker openWeatherCircuitBreaker) {
        this.openWeatherRestClient = openWeatherRestClient;
        this.openWeatherCircuitBreaker = openWeatherCircuitBreaker;
    }

    @Override
    public OpenWeatherForecastResponse fetchWeatherForecastOfNext4Days(CityName cityName) {
        return openWeatherCircuitBreaker.decorateSupplier(() ->
            openWeatherRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment("forecast")
                            .queryParam("q", cityName.getValue())
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<OpenWeatherForecastResponse>() {})
                    .getBody()
        ).get();
    }
}
