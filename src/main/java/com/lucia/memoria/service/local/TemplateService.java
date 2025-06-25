package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.TemplateFieldDTO;
import com.lucia.memoria.dto.local.TemplateDTO;
import com.lucia.memoria.exception.NotFoundException;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TemplateService {

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
      throw new IllegalArgumentException("Template with that name already exists");
    }

    Template template = new Template();
    template.setTemplateId(UUID.randomUUID());
    template.setName(templateDTO.getName());
    template.setOwner(owner);

    for (TemplateFieldDTO templateFieldDTO : templateDTO.getFields()) {
      TemplateField templateField = templateFieldMapper.toEntity(templateFieldDTO);
      templateField.setTemplateFieldId(UUID.randomUUID());
      template.addField(templateField);
    }

    return templateMapper.toDTO(templateRepository.save(template));
  }

  public TemplateDTO getTemplateById(UUID templateId) {
    Optional<Template> template = templateRepository.findByTemplateId(templateId);
    if (template.isPresent()) {
      return templateMapper.toDTO(template.get());
    } else {
      throw new NoSuchElementException("Template Not found");
    }
  }


  public Template getTemplateEntityById(UUID templateId) {
    Optional<Template> template = templateRepository.findByTemplateId(templateId);
    if (template.isPresent()) {
      return template.get();
    } else {
      throw new NoSuchElementException("Template Not found");
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
  public Template getTemplateWithFieldsById(UUID templateId) {
    return templateRepository.findTemplateByTemplateIdWithFields(
            templateId)
        .orElseThrow(() -> new NotFoundException("Template not found"));
  }

  @Transactional(readOnly = true)
  public List<TemplateDTO> getTemplatesByUserId(UUID userId) {
    User owner = userService.getUserEntityById(userId);

    return templateMapper.toDTOList(templateRepository.findAllByOwner(owner));
  }

}
