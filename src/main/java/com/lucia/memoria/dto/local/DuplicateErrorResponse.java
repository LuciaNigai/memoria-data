package com.lucia.memoria.dto.local;

import java.util.List;

public record DuplicateErrorResponse(String message, List<Object> duplicates) {

}

