package com.progra3.app.controller;

import com.progra3.app.controller.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = resolveStatus(ex);
        String message = safeMessage(ex.getMessage(), status);
        return buildErrorResponse(status, message, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .distinct()
                .collect(Collectors.joining("; "));
        if (message.isBlank()) {
            message = "Solicitud invalida";
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Cuerpo de solicitud invalido", request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(status.value());
        error.setError(status.getReasonPhrase());
        error.setMessage(message);
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    private HttpStatus resolveStatus(IllegalArgumentException ex) {
        String message = ex.getMessage();
        if (message == null) {
            return HttpStatus.BAD_REQUEST;
        }
        String normalized = message.toLowerCase(Locale.ROOT);
        return normalized.contains("no encontrado") || normalized.contains("not found")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;
    }

    private String safeMessage(String message, HttpStatus status) {
        if (message != null && !message.isBlank()) {
            return message;
        }
        return status == HttpStatus.NOT_FOUND ? "Recurso no encontrado" : "Solicitud invalida";
    }

    private String formatFieldError(FieldError error) {
        String field = error.getField();
        String detail = error.getDefaultMessage();
        if (detail == null || detail.isBlank()) {
            detail = "valor invalido";
        }
        return field + ": " + detail;
    }
}

