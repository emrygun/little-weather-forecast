package dev.emrygun.forecast.converter;

import dev.emrygun.forecast.model.CityName;
import org.springframework.core.convert.converter.Converter;

public class CityNameConverter {
    public static final CityNameToStringConverter CITY_NAME_TO_STRING_CONVERTER = CityNameToStringConverter.INSTANCE;
    public static final StringToCityNameConverter STRING_TO_CITY_NAME_CONVERTER = StringToCityNameConverter.INSTANCE;

    public enum CityNameToStringConverter implements Converter<CityName, String> {
        INSTANCE;

        @Override
        public String convert(CityName source) {
            return source.getValue();
        }
    }

    public enum StringToCityNameConverter implements Converter<String, CityName> {
        INSTANCE;

        @Override
        public CityName convert(String source) {
            return new CityName(source);
        }
    }
}
