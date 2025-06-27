package com.lucia.memoria.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException {
  private final List<Object> duplicates;

  public DuplicateException(String message, List<Object> duplicates) {
    super(message);
    this.duplicates = duplicates;
  }

  public List<Object> getDuplicates() {
    return duplicates;
  }

}

