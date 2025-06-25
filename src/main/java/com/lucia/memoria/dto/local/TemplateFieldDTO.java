package com.lucia.memoria.dto.local;

import com.lucia.memoria.helper.FieldRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateFieldDTO {

  private UUID templateFieldId;
  String name;
  private FieldRole fieldRole;
}
