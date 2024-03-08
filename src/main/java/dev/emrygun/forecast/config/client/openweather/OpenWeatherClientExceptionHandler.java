package dev.emrygun.forecast.config.client.openweather;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.emrygun.forecast.exception.RemoteWeatherServerException;
import dev.emrygun.forecast.exception.UnexpectedWeatherServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;
import java.util.UUID;

public class OpenWeatherClientExceptionHandler extends DefaultResponseErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OpenWeatherClientConfiguration.class);

    @Override
    public boolean hasError(ClientHttpResponse chr) throws IOException {
        return chr.getStatusCode().is4xxClientError() || chr.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getStatusCode().value());
        if (statusCode == null) {
            throw new UnknownHttpStatusCodeException(
                    response.getStatusCode().value(),
                    response.getStatusText(),
                    response.getHeaders(),
                    this.getResponseBody(response),
                    this.getCharset(response)
            );
        }
        switch (statusCode.series()) {
            case CLIENT_ERROR -> {
                String errorMessage = "Unknown OpenWeather client error.";
                try {
                    JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
                    var clientErrorMessageNode = jsonNode.get("message");
                    if (clientErrorMessageNode != null) errorMessage = clientErrorMessageNode.asText();
                } catch (IOException ignored) { }

                throw new RemoteWeatherServerException(
                        errorMessage,
                        UUID.randomUUID().toString()
                );
            }
            case SERVER_ERROR -> {
                String errorMessage = "Unexpected OpenWeather server error.";
                try {
                    JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
                    LOG.error("Unexpected error from OpenWeather server: %s".formatted(jsonNode.toPrettyString()));
                } catch (IOException ignored) { }

                throw new UnexpectedWeatherServerException(
                        errorMessage,
                        UUID.randomUUID().toString()
                );
            }
        }
    }
}