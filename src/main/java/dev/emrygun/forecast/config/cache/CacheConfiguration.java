package dev.emrygun.forecast.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableCaching
public class CacheConfiguration {
    public static final String WEATHER_FORECAST = "WEATHER_FORECAST";

    private final CacheProperties cacheProperties;

    public CacheConfiguration(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(WEATHER_FORECAST);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(cacheProperties.getTimeToLive())
                .weakKeys()
                .recordStats());

        return cacheManager;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void refresh() {
        cacheManager().getCache(WEATHER_FORECAST).invalidate();
    }
}
