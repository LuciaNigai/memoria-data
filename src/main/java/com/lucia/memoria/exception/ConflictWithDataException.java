package com.lucia.memoria.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class ConflictWithDataException extends RuntimeException {
  private final List<?> data;

  public ConflictWithDataException(String message, List<?> data) {
    super(message);
    this.data = data;
  }
}
