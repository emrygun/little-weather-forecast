package dev.emrygun.forecast.controller;

import dev.emrygun.forecast.model.CityName;
import dev.emrygun.forecast.dto.WeatherForecastReport;
import dev.emrygun.forecast.exception.CityNotFoundException;
import dev.emrygun.forecast.service.WeatherForecastService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherForecastController.class)
class WeatherForecastControllerTest {

    private static final String FORECAST_SERVICE_ENDPOINT = "/v1/weather-forecasts";

    @MockBean
    private WeatherForecastService weatherForecastService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShouldReturn404NotFound() throws Exception {
        Mockito.when(weatherForecastService.getWeatherForecastOfNext48Hours(ArgumentMatchers.any()))
                .thenThrow(new CityNotFoundException(new CityName("Test")));

        mockMvc.perform(get(FORECAST_SERVICE_ENDPOINT + "/Test").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testShouldReturn200Ok() throws Exception {
        Mockito.when(weatherForecastService.getWeatherForecastOfNext48Hours(ArgumentMatchers.any()))
                .thenReturn(new WeatherForecastReport(null, null));

        mockMvc.perform(get(FORECAST_SERVICE_ENDPOINT + "/İstanbul").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}