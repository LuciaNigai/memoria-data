package com.lucia.memoria.helper;

import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
@ToString
public class TemplateFieldType {
   private FieldType fieldType;
   private List<String> options;

  public TemplateFieldType(FieldType fieldType) {
    this(fieldType, List.of());
  }

   public TemplateFieldType(FieldType fieldType, List<String> options) {
     this.fieldType = fieldType;
     this.options = options;
   }
}
