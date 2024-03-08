package dev.emrygun.forecast.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import dev.emrygun.forecast.client.openweather.OpenWeatherForecastClient;
import dev.emrygun.forecast.client.openweather.OpenWeatherForecastClientImpl;
import dev.emrygun.forecast.config.AppConfig;
import dev.emrygun.forecast.config.client.openweather.OpenWeatherClientConfiguration;
import dev.emrygun.forecast.config.client.openweather.OpenWeatherClientConfigurationProperties;
import dev.emrygun.forecast.config.resilience.CircuitBreakerConfiguration;
import dev.emrygun.forecast.exception.RemoteWeatherServerException;
import dev.emrygun.forecast.exception.UnexpectedWeatherServerException;
import dev.emrygun.forecast.model.CityName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(
        initializers = {OpenWeatherForecastClientTest.WireMockInitializer.class},
        classes = {
                OpenWeatherClientConfiguration.class,
                OpenWeatherClientConfigurationProperties.class,
                OpenWeatherForecastClientImpl.class,
                CircuitBreakerConfiguration.class,
                OpenWeatherForecastClientTest.WireMockInitializer.class,
                AppConfig.class
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ConfigurationPropertiesScan
class OpenWeatherForecastClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(OpenWeatherForecastClientTest.class);

    @Autowired
    private OpenWeatherForecastClient client;

    @Autowired
    private ObjectMapper mapper;

    public static class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static Logger LOG = LoggerFactory.getLogger(WireMockInitializer.class);

        private static final String FORECAST_SERVICE_ENDPOINT = "/forecast";
        private final String sampleJsonResponseForAnkara;

        public WireMockInitializer() throws IOException {
            var sampleResponseStream = this.getClass().getClassLoader().getResourceAsStream("sample-openweather-response.json");
            sampleJsonResponseForAnkara = new String(sampleResponseStream.readAllBytes(), StandardCharsets.UTF_8);
        }


        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
            wireMockServer.stubFor(
                    get(urlPathEqualTo(FORECAST_SERVICE_ENDPOINT))
                            .withQueryParam("q", equalTo("Ankara"))
                            .withQueryParam("APPID", equalTo("123"))
                            .willReturn(okJson(sampleJsonResponseForAnkara))
            );
            wireMockServer.stubFor(
                    get(urlPathEqualTo(FORECAST_SERVICE_ENDPOINT))
                            .withQueryParam("q", equalTo("Unknown"))
                            .withQueryParam("APPID", equalTo("123"))
                            .willReturn(notFound())
            );
            wireMockServer.stubFor(
                    get(urlPathEqualTo(FORECAST_SERVICE_ENDPOINT))
                            .withQueryParam("q", equalTo("Error"))
                            .withQueryParam("APPID", equalTo("123"))
                            .willReturn(serverError())
            );
            wireMockServer.start();

            TestPropertyValues
                    .of(Map.of(
                            "little-weather-forecast.client.open-weather.base-url", wireMockServer.baseUrl(),
                            "little-weather-forecast.client.open-weather.api-key", "123"
                    ))
                    .applyTo(applicationContext);

            LOG.info("Wiremock running on: %s".formatted(wireMockServer.baseUrl()));
        }
    }

    @Test
    @DisplayName("Should fetch weather forecast data from OpenWeather")
    void shouldFetchWeatherForecast() throws JsonProcessingException {
        var resp = client.fetchWeatherForecastOfNext4Days(new CityName("Ankara"));

        assertEquals("Ankara", resp.cityInformation().name());
        assertEquals("TR", resp.cityInformation().country());

        var firstWeatherInfo = resp.weatherInformation().get(0);

        assertEquals(276.8F, firstWeatherInfo.weatherForecastInformation().feelsLike());
        assertEquals(279.76F, firstWeatherInfo.weatherForecastInformation().maximum());
        assertEquals(88F, firstWeatherInfo.weatherForecastInformation().humidity());
        assertFalse(resp.weatherInformation().isEmpty());

        LOG.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp));
    }

    @Test
    @DisplayName("Should not fetch weather forecast data from OpenWeather")
    void shouldNotFetchWeatherForecast() {
        assertThrows(
                RemoteWeatherServerException.class,
                () -> client.fetchWeatherForecastOfNext4Days(new CityName("Unknown"))
        );
        assertThrows(
                UnexpectedWeatherServerException.class,
                () -> client.fetchWeatherForecastOfNext4Days(new CityName("Error"))
        );
    }
}