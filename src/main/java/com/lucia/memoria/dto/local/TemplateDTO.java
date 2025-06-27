package com.lucia.memoria.dto.local;

import jakarta.validation.constraints.NotBlank;
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
public class TemplateDTO {

  private UUID templateId;
  @NotNull
  private UUID ownerId;
  @NotBlank
  String name;
  List<TemplateFieldDTO> fields;
  Boolean includesPartOfSpeech;
}
