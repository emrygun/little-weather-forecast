package dev.emrygun.forecast.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "little-weather-forecast.cache")
public class CacheProperties {
    private Duration timeToLive = Duration.ofHours(1);

    public CacheProperties() {
    }

    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Duration getTimeToLive() {
        return timeToLive;
    }
}
