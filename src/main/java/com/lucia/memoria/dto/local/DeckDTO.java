package com.lucia.memoria.dto.local;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeckDTO {

  private UUID deckId;
  private UUID userId;
  private String name;
  private String path;
  private List<DeckDTO> childDecks;

}
