package com.lucia.memoria.repository;

import com.lucia.memoria.helper.FieldRole;
import com.lucia.memoria.model.FieldTemplate;
import com.lucia.memoria.model.Template;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldTemplateRepository extends JpaRepository<FieldTemplate, Long> {
  Optional<FieldTemplate> findByNameAndFieldRole(String name, FieldRole fieldRole);

  Optional<FieldTemplate> findByFieldTemplateId(UUID fieldTemplateId);

  Optional<FieldTemplate> findByFieldTemplateIdAndTemplate(UUID fieldTemplateId, Template template);
}
