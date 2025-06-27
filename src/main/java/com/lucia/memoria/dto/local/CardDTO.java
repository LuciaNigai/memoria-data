package com.lucia.memoria.dto.local;


import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

  private UUID cardId;
  @NotNull
  UUID deckId;
  @NotNull
  UUID templateId;
  List<FieldDTO> fieldDTOList;
}
