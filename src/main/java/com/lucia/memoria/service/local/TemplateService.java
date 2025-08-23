package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.dto.local.ResponseWithListDTO;
import com.lucia.memoria.dto.local.TemplateFieldDTO;
import com.lucia.memoria.dto.local.TemplateDTO;
import com.lucia.memoria.exception.NotFoundException;
import com.lucia.memoria.helper.FieldRole;
import com.lucia.memoria.helper.FieldType;
import com.lucia.memoria.helper.TemplateFieldType;
import com.lucia.memoria.mapper.CardMapper;
import com.lucia.memoria.mapper.TemplateFieldMapper;
import com.lucia.memoria.mapper.TemplateMapper;
import com.lucia.memoria.model.Card;
import com.lucia.memoria.model.Field;
import com.lucia.memoria.model.TemplateField;
import com.lucia.memoria.model.Template;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.CardRepository;
import com.lucia.memoria.repository.TemplateRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TemplateService {

  private static final List<String> PARTS_OF_SPEECH = List.of(
      "noun", "pronoun", "verb", "adjective", "adverb", "preposition", "conjunction", "interjection"
  );


  private final TemplateRepository templateRepository;
  private final CardRepository cardRepository;
  private final UserService userService;
  private final TemplateFieldService templateFieldService;
  private final TemplateFieldMapper templateFieldMapper;
  private final TemplateMapper templateMapper;
  private final CardMapper cardMapper;

  public TemplateService(TemplateRepository templateRepository, CardRepository cardRepository,
      UserService userService, TemplateFieldService templateFieldService,
      TemplateFieldMapper templateFieldMapper, TemplateMapper templateMapper,
      CardMapper cardMapper) {
    this.templateRepository = templateRepository;
    this.cardRepository = cardRepository;
    this.userService = userService;
    this.templateFieldService = templateFieldService;
    this.templateFieldMapper = templateFieldMapper;
    this.templateMapper = templateMapper;
    this.cardMapper = cardMapper;
  }

  @Transactional
  public TemplateDTO createTemplate(TemplateDTO templateDTO) {
    User owner = userService.getUserEntityById(templateDTO.getOwnerId());

    Optional<Template> templateExists = templateRepository.findByNameAndOwner(templateDTO.getName(),
        owner);

    if (templateExists.isPresent()) {
      throw new IllegalArgumentException(
          "Template with name " + templateDTO.getName() + " already exists");
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

  @Transactional(readOnly = true)
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

  @Transactional
  public ResponseWithListDTO<?> deleteTemplate(UUID templateId) {
    Template template = templateRepository.findTemplateByTemplateIdWithFields(templateId)
        .orElse(null);
    if (template == null) {
      return new ResponseWithListDTO<Object>(
          "Template you are trying to delete does not exist",
          HttpStatus.NOT_FOUND,
          new ArrayList<>()
      );
    }

    List<Card> templateCards = cardRepository.findByTemplate(template);
    if (!templateCards.isEmpty()) {
      return new ResponseWithListDTO<CardMinimalDTO>(
          "Template cannot be deleted. There are still cards that use that template.",
          HttpStatus.CONFLICT,
          cardMapper.toMinimalDTOList(templateCards)
      );
    }

    for (TemplateField templateField : template.getFields()) {
      templateFieldService.deleteTemplateField(templateField);
    }

    templateRepository.delete(template);
    return new ResponseWithListDTO<Object>("Template Successfully deleted", HttpStatus.OK,
        new ArrayList<>());
  }

}
