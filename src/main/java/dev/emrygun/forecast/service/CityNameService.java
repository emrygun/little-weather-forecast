package dev.emrygun.forecast.service;

import dev.emrygun.forecast.model.CityName;

public interface CityNameService {
    boolean isCityNameExists(CityName cityName);
}
