package com.lucia.memoria.dto.local;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardMinimalDTO {
    private UUID cardId;
    private String front;
    private String back;
}
