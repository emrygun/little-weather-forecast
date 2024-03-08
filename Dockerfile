FROM eclipse-temurin:21-jdk-alpine as prod
RUN mkdir /app
COPY *.jar /little-weather-forecast.jar
ENV SERVER_PORT=8080
ENV SERVER_FORWARD_HEADERS_STRATEGY=framework
ENV SERVER_SERVLET_CONTEXT_PATH=/little-weather-forecast
ENV LITTLE_WEATHER_FORECAST_CLIENT_OPEN_WEATHER_API_KEY=${API_KEY}
WORKDIR /app
ENTRYPOINT ["java","-jar","/little-weather-forecast.jar"]
