package dev.emrygun.forecast.controller;

import dev.emrygun.forecast.model.CityName;
import dev.emrygun.forecast.service.WeatherForecastService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import dev.emrygun.forecast.config.apidoc.ApiTags;
import dev.emrygun.forecast.dto.WeatherForecastReport;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/v1/weather-forecasts")
@Tag(name = ApiTags.WEATHER_FORECAST)
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    public WeatherForecastController(WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    /**
     * GET /city/{cityName} : Get weather forecast of next 48 hours by city.
     *
     *
     * @param cityName Name of the city that will be issued for weather information.
     * @return Weather forecast (status code 200)
     *         or Not Found if {@code cityName} is not exists (status code 404)
     *         or Internal Server Error if unexpected error occurs (status code 500).
     */
    @Operation(
        operationId = "getWeatherForecastByCity",
        summary     = "Get cities weather forecasts for the next 48 hours",
        responses = {
            @ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
            )
        }
    )
    @GetMapping(value = "/{cityName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WeatherForecastReport getWeatherForecastByCity(
            @PathVariable CityName cityName
    ) {
        return weatherForecastService.getWeatherForecastOfNext48Hours(cityName);
    }
}
