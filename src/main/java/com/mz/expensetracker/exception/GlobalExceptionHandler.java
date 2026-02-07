package com.mz.expensetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Component("expenseGlobalExceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleRuntime(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "error", e.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        Map<String,Object> res = new HashMap<>();
        res.put("timestamp", LocalDateTime.now());
        res.put("status", HttpStatus.BAD_REQUEST.value());
        res.put("errors", errors);
        return ResponseEntity.badRequest().body(res);
    }

    private ResponseEntity<Map<String,Object>> buildResponseEntity(HttpStatus status,String message) {
        Map<String,Object> res = new HashMap<>();
        res.put("timestamp", LocalDateTime.now());
        res.put("status", status.value());
        res.put("message", message);
        return new ResponseEntity<>(res, status);
    }
}
