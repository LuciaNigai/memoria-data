package com.lucia.memoria.repository;

import com.lucia.memoria.model.Template;
import com.lucia.memoria.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TemplateRepository extends JpaRepository<Template, Long> {

  Optional<Template> findByNameAndOwner(String name, User owner);

  Optional<Template> findByTemplateId(UUID templateId);

  List<Template> findAllByOwner(User owner);

  @Query("""
          SELECT t FROM Template t
          JOIN FETCH t.fields
          WHERE t.templateId = :templateId
      """)
  Optional<Template> findTemplateByTemplateIdWithFields(UUID templateId);
}
