package dev.emrygun.forecast.exception;

public class RemoteWeatherServerException extends RuntimeException {

    private final String tracker;

    public RemoteWeatherServerException(String message, String tracker) {
        super(message);
        this.tracker = tracker;
    }

    public RemoteWeatherServerException(String message, Throwable cause, String tracker) {
        super(message, cause);
        this.tracker = tracker;
    }
}
