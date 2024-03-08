package dev.emrygun.forecast.exception;

import dev.emrygun.forecast.model.CityName;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(CityName cityName) {
       super("City with name '%s' not found.".formatted(cityName));
    }
}
