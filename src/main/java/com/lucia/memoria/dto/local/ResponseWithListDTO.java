package com.lucia.memoria.dto.local;

import java.util.List;
import org.springframework.http.HttpStatus;

public record ResponseWithListDTO<T>(String message, HttpStatus status, List<T> objects) {

}
