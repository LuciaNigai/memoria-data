package com.lucia.memoria.dto.local;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldMinimalDTO {
  private String content;
  UUID fieldTemplateId;
}
