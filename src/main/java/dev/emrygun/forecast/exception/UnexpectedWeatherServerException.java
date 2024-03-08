package dev.emrygun.forecast.exception;

public class UnexpectedWeatherServerException extends RuntimeException {

    private final String tracker;

    public UnexpectedWeatherServerException(String message, String tracker) {
        super(message);
        this.tracker = tracker;
    }

    public UnexpectedWeatherServerException(String message, Throwable cause, String tracker) {
        super(message, cause);
        this.tracker = tracker;
    }
}
