package com.lucia.memoria.exception;

import com.lucia.memoria.dto.local.DuplicateErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity
        .badRequest()
        .body(ex.getMessage());
  }

  @ExceptionHandler(DuplicateException.class)
  public ResponseEntity<DuplicateErrorResponse> handleDuplicateException(DuplicateException ex) {
    DuplicateErrorResponse response = new DuplicateErrorResponse(
        ex.getMessage(),
        ex.getDuplicates()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }
}
