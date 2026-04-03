package com.company.platform.common.api.exception;

import com.company.platform.common.api.response.ApiErrorResponse;
import com.company.platform.common.api.response.ApiResponseFactory;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseFactory.error("Resource not found", ex.getMessage()));
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseFactory.error("Validation error", ex.getMessage()));
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponseFactory.error("Business error", ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(", "));

        if(errorMessage.isBlank()) {
            errorMessage = "Request validation error";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseFactory.error("Validation error", errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {

        String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .distinct()
                .collect(Collectors.joining(", "));

        if (errorMessage.isBlank()) {
            errorMessage = "Constraint violation";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseFactory.error("Validation error", errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseFactory.error(ex.getMessage(), "Unexpected internal error"));
    }
}
