package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.TemplateFieldDTO;
import com.lucia.memoria.dto.local.TemplateDTO;
import com.lucia.memoria.exception.NotFoundException;
import com.lucia.memoria.helper.FieldRole;
import com.lucia.memoria.helper.FieldType;
import com.lucia.memoria.helper.TemplateFieldType;
import com.lucia.memoria.mapper.TemplateFieldMapper;
import com.lucia.memoria.mapper.TemplateMapper;
import com.lucia.memoria.model.TemplateField;
import com.lucia.memoria.model.Template;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.TemplateRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TemplateService {

  private static final List<String> PARTS_OF_SPEECH = List.of(
      "noun", "pronoun", "verb", "adjective", "adverb", "preposition", "conjunction", "interjection"
  );


  private final TemplateRepository templateRepository;
  private final UserService userService;
  private final TemplateFieldMapper templateFieldMapper;
  private final TemplateMapper templateMapper;

  public TemplateService(TemplateRepository templateRepository, UserService userService,
      TemplateFieldMapper templateFieldMapper, TemplateMapper templateMapper) {
    this.templateRepository = templateRepository;
    this.userService = userService;
    this.templateFieldMapper = templateFieldMapper;
    this.templateMapper = templateMapper;
  }

  @Transactional
  public TemplateDTO createTemplate(TemplateDTO templateDTO) {
    User owner = userService.getUserEntityById(templateDTO.getOwnerId());

    Optional<Template> templateExists = templateRepository.findByNameAndOwner(templateDTO.getName(),
        owner);

    if (templateExists.isPresent()) {
      throw new IllegalArgumentException("Template with name " + templateDTO.getName() + " already exists");
    }

    Template template = new Template();
    template.setTemplateId(UUID.randomUUID());
    template.setName(templateDTO.getName());
    template.setOwner(owner);

    for (TemplateFieldDTO templateFieldDTO : templateDTO.getFields()) {
      addTemplateField(templateFieldDTO, template);
    }

    boolean hasPOS = template.getFields().stream()
        .anyMatch(f -> "Part of Speech".equalsIgnoreCase(f.getName()));
    if (Boolean.TRUE.equals(templateDTO.getIncludesPartOfSpeech()) && !hasPOS) {
      addPartOfSpeechFieldIfNeeded(templateDTO, template);
    }

    return templateMapper.toDTO(templateRepository.save(template));
  }

  private void addTemplateField(TemplateFieldDTO templateFieldDTO, Template template) {
    TemplateFieldType templateFieldType =
        templateFieldDTO.getTemplateFieldType() == null ? new TemplateFieldType(FieldType.TEXT)
            : templateFieldDTO.getTemplateFieldType();
    TemplateField templateField = templateFieldMapper.toEntity(templateFieldDTO);
    templateField.setTemplateFieldId(UUID.randomUUID());
    templateField.setTemplateFieldType(templateFieldType);
    template.addField(templateField);
  }

  private static void addPartOfSpeechFieldIfNeeded(TemplateDTO templateDTO, Template template) {
    if (Boolean.TRUE.equals(templateDTO.getIncludesPartOfSpeech())) {
      TemplateField templateField = new TemplateField();
      templateField.setTemplateFieldId(UUID.randomUUID());
      templateField.setName("Part of Speech");
      templateField.setFieldRole(FieldRole.AUXILIARY);
      templateField.setTemplateFieldType(new TemplateFieldType(
          FieldType.ENUM,
          PARTS_OF_SPEECH
      ));
      template.addField(templateField);
      template.setIncludesPartOfSpeech(true);
    }
  }

  public TemplateDTO getTemplateById(UUID templateId) {
    return templateMapper.toDTO(getTemplateEntityById(templateId));
  }

  public Template getTemplateEntityById(UUID templateId) {
    Optional<Template> template = templateRepository.findByTemplateId(templateId);
    if (template.isPresent()) {
      return template.get();
    } else {
      throw new NotFoundException("Template Not found");
    }
  }

  @Transactional(readOnly = true)
  public List<TemplateDTO> getTemplatesByUserId(UUID userId) {
    User owner = userService.getUserEntityById(userId);

    return templateMapper.toDTOList(templateRepository.findAllByOwner(owner));
  }

}
