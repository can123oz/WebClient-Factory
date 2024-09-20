package com.WebClientFactory.WebClient_Factory.exception;

import com.WebClientFactory.WebClient_Factory.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({ClientException.class})
    public ResponseEntity<?> handleException(RuntimeException runtimeException,
                                             HttpServletRequest httpServletRequest) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        runtimeException.printStackTrace(printWriter);
        ApiErrorResponse apiError = new ApiErrorResponse(
                httpServletRequest.getRequestURI(),
                runtimeException.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value(),
                stringWriter.toString());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CircuitBreakerException.class)
    public ResponseEntity<?> handleException(CircuitBreakerException circuitBreakerException,
                                             HttpServletRequest httpServletRequest) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        circuitBreakerException.printStackTrace(printWriter);
        ApiErrorResponse apiError = new ApiErrorResponse(
                httpServletRequest.getRequestURI(),
                circuitBreakerException.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                stringWriter.toString());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
