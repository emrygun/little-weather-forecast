package dev.emrygun.forecast.mapper;

import dev.emrygun.forecast.model.CityName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {
        CityNameMapperImpl.class
})
@ExtendWith(SpringExtension.class)
class CityNameMapperTest {

    @Autowired
    private CityNameMapper cityNameMapper;

    @Test
    @DisplayName("Should map string into CityName")
    void shouldMapStringIntoCityName() {
        assertEquals(new CityName("İstanbul"), cityNameMapper.toCityName("İstanbul"));
    }
}