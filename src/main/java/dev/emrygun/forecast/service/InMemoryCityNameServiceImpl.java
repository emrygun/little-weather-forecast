package dev.emrygun.forecast.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import dev.emrygun.forecast.config.InMemoryCityNameServiceConfiguration;
import dev.emrygun.forecast.model.City;
import dev.emrygun.forecast.model.CityName;
import dev.emrygun.forecast.exception.CityNameResourceLoadException;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InMemoryCityNameServiceImpl implements CityNameService {
    private final ObjectMapper objectMapper;
    private final InMemoryCityNameServiceConfiguration inMemoryCityNameServiceConfiguration;
    private final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private Map<String, List<City>> cities;

    public InMemoryCityNameServiceImpl(ObjectMapper objectMapper,
                                       InMemoryCityNameServiceConfiguration inMemoryCityNameServiceConfiguration) {
        this.objectMapper = objectMapper;
        this.inMemoryCityNameServiceConfiguration = inMemoryCityNameServiceConfiguration;
    }

    @PostConstruct
    private void init() throws CityNameResourceLoadException {
        loadCities();
    }

    @Override
    public boolean isCityNameExists(CityName cityName) {
        return cities.containsKey(cityName.getValue());
    }

    public void refreshCitiesRegistry() throws CityNameResourceLoadException {
        loadCities();
    }

    // FIXME: Exception handling
    private void loadCities() throws CityNameResourceLoadException {
        try {
            var citiesResource = resourceResolver.getResource(inMemoryCityNameServiceConfiguration.getCityListResourcePath());
            var newCities = objectMapper.readValue(citiesResource.getInputStream(), new TypeReference<List<City>>() {});

            cities = newCities.stream()
                    .distinct()
                    .collect(Collectors.groupingBy(City::name));
        } catch (IOException e) {
            throw new CityNameResourceLoadException(
                    "Error while loading city nmaes from path: %s"
                            .formatted(inMemoryCityNameServiceConfiguration.getCityListResourcePath())
            );
        }
    }

}
