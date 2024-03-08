package dev.emrygun.forecast.config.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import dev.emrygun.forecast.exception.UnexpectedWeatherServerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreaker openWeatherRemoteServerCircuitBreaker() {
        CircuitBreakerConfig config = new CircuitBreakerConfig.Builder()
                .failureRateThreshold(50)
                .slidingWindow(20, 5, COUNT_BASED)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .recordExceptions(UnexpectedWeatherServerException.class)
                .build();

        return CircuitBreaker.of("open-weather-remote-server", config);
    }
}
