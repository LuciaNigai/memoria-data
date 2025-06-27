package com.lucia.memoria.dto.local;

import org.springframework.http.HttpStatus;

public record GeneralResponseDTO(String message, HttpStatus status) {

}
