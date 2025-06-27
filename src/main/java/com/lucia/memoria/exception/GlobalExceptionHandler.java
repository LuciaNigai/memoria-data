package com.lucia.memoria.exception;

import com.lucia.memoria.dto.local.DuplicateErrorResponseDTO;
import com.lucia.memoria.dto.local.GeneralResponseDTO;
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
  public ResponseEntity<DuplicateErrorResponseDTO> handleDuplicateException(DuplicateException ex) {
    DuplicateErrorResponseDTO response = new DuplicateErrorResponseDTO(
        ex.getMessage(),
        ex.getDuplicates()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<GeneralResponseDTO> handleDuplicateException(IllegalArgumentException ex) {
    GeneralResponseDTO response = new GeneralResponseDTO(
        ex.getMessage(),
        HttpStatus.BAD_REQUEST
    );
    return ResponseEntity.badRequest().body(response);
  }
}
