package org.ilisi.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(GlobalAppException.class)
    public ResponseEntity<Map<String, Object>> handleGlobalAppException(GlobalAppException e) {
        Map<String, Object> body = Map.of(
                "message", e.getMessage(),
                "errorCode", e.getErrorCode(),
                "timestamp", e.getTimestamp()
        );
        log.error("Global app exception", e);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        Map<String, Object> body = Map.of("message", e.getMessage());
        log.error("Authentication failed", e);
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<Map<String, Object>> handleStorageException(StorageException e) {
        Map<String, Object> body = Map.of("message", e.getMessage());
        log.error("Storage exception", e);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, Object> body = Map.of("message", e.getMessage());
        log.error("Illegal argument", e);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {


        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        log.error("Method argument not valid", e);
        return new ResponseEntity<>(Map.of(
                "message", "Input validation failed",
                "errors", errors
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        // add the cause of the exeption in the returned body
        Map<String, Object> body = Map.of("message", e.getMessage());
        log.error("Exception", e);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


}