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
public class DeckMinimalDTO {

  private UUID deckId;
  private String name;
  private String path;
  private UUID userId;
}
