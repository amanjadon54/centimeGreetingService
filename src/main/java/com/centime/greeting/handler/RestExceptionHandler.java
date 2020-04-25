package com.centime.greeting.handler;

import com.centime.util.exception.ApiError;
import com.centime.util.exception.CustomRuntimeException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ApiError> handleRestException(
            CustomRuntimeException e, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.valueOf(e.getStatus()),e.getMessage(),e.getDeveloperMsg());

        return new ResponseEntity(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleJsonException(
            Exception e, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e.getMessage());

        return new ResponseEntity(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleRestException(
            Exception e, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        return new ResponseEntity(apiError, apiError.getHttpStatus());
    }

}
