package com.lucia.memoria.dto.local;

import java.util.List;

public record DuplicateErrorResponseDTO(String message, List<Object> duplicates) {

}

