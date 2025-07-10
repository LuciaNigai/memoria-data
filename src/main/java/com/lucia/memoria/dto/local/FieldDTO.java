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
public class FieldDTO {

  TemplateFieldDTO fieldTemplate;
  private UUID fieldId;
  String content;

  public static FieldDTO blankWithTemplate(TemplateFieldDTO templateFieldDTO) {
    FieldDTO dto = new FieldDTO();
    dto.setContent(null);
    dto.setFieldTemplate(templateFieldDTO);
    return dto;
  }
}
