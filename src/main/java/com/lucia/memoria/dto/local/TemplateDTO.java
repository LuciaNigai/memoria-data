package com.lucia.memoria.dto.local;

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
  private UUID ownerId;
  String name;
  List<TemplateFieldDTO> fields;
}
