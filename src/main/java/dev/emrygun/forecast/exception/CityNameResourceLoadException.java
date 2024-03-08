package dev.emrygun.forecast.exception;

public class CityNameResourceLoadException extends Exception {
    public CityNameResourceLoadException(String message) {
        super(message);
    }

    public CityNameResourceLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
