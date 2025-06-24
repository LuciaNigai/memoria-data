package com.lucia.memoria.repository;

import com.lucia.memoria.helper.FieldRole;
import com.lucia.memoria.model.TemplateField;
import com.lucia.memoria.model.Template;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateFieldRepository extends JpaRepository<TemplateField, Long> {

  Optional<TemplateField> findByNameAndFieldRole(String name, FieldRole fieldRole);

  Optional<TemplateField> findByFieldTemplateId(UUID fieldTemplateId);

  Optional<TemplateField> findByFieldTemplateIdAndTemplate(UUID fieldTemplateId, Template template);
}
