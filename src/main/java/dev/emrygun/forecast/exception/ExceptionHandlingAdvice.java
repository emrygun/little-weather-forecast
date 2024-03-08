package dev.emrygun.forecast.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleCityNotFoundException(final CityNotFoundException exception, WebRequest request) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Resource Not Found");
        var errorResponseException = new ErrorResponseException(HttpStatus.valueOf(problem.getStatus()), problem, exception);
        return handleProblemInternal(errorResponseException, request);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleOtherExceptions(final Exception exception, WebRequest request) {
        logger.error("Unknown Error:", exception);
        var problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Unexpected Exception");
        var errorResponseException = new ErrorResponseException(HttpStatus.valueOf(problem.getStatus()), problem, exception);
        return handleProblemInternal(errorResponseException, request);
    }

    protected ResponseEntity<Object> handleProblemInternal(ErrorResponseException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(ex, null, headers, ex.getStatusCode(), request);
    }
}
