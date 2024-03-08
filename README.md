# (Yet Another) Little Weather Forecast

## Summary
"Little Weather Forecast" is a demo service serving weather forecast information of cities with remote weather service connections.

## Installation
Use the project management tool [maven](https://maven.apache.org/) to install "Little Weather Forecast".
```shell
$ mvn clean install
```

## Usage

### Deployment
You may deploy this application by packaged `jar` file.
```shell
$ java -jar little-weather-forecast-{version}.jar
```

Or you may use the `Dockerfile` as well.

### Remote services
Since it uses [OpenWeatherMap API](https://openweathermap.org/api), you'll need to provide `api-key` to use the project through 
spring boot configuration file or environment variable.

`application.properties`:
```properties
little-weather-forecast.client.open-weather.api-key=${API_KEY}
```
environment variable:
```shell
LITTLE_WEATHER_FORECAST_CLIENT_OPEN_WEATHER_API_KEY=${API_KEY}
```
Dockerfile environment variable:
```dockerfile
ENV LITTLE_WEATHER_FORECAST_CLIENT_OPEN_WEATHER_API_KEY=${API_KEY}
```

### API Docs
This project uses [Open Api 3](https://spec.openapis.org/oas/v3.1.0) specifications for api documentations. 

You may reach documentations on your `target` directory or directly on application instance through following path:

`/api/docs`

And Swagger UI from:

`/api/docs/swagger-ui`

### Example
Request for weather forecast information of Ankara for next 48 hours:
```http request
GET /v1/weather-forecasts/Ankara
```
Response:
```json
{
  "city": "Ankara",
  "weatherForecasts": [
    {
      "dateTime": "2024-03-08T18:00:00Z",
      "maximumTemp": 280.49,
      "feelsLikeTemp": 277.44,
      "humidity": 63
    },
    {
      "dateTime": "2024-03-08T21:00:00Z",
      "maximumTemp": 279.42,
      "feelsLikeTemp": 277.91,
      "humidity": 67
    },
    ...
```

## TODO
* Apache HttpClient5 adaptation, configurations.
* Request logging and metrics, `logbook` adaptation.
* Resiliency tests.
* Invalid api key scenarios.
* Minor refactors.
