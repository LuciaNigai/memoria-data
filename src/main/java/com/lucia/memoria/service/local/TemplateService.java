package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.FieldTemplateDTO;
import com.lucia.memoria.dto.local.TemplateDTO;
import com.lucia.memoria.mapper.FieldTemplateMapper;
import com.lucia.memoria.mapper.TemplateMapper;
import com.lucia.memoria.model.FieldTemplate;
import com.lucia.memoria.model.Template;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.TemplateRepository;
import com.lucia.memoria.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {

  private final UserRepository userRepository;
  private final TemplateRepository templateRepository;
  private final FieldTemplateMapper fieldTemplateMapper;
  private final TemplateMapper templateMapper;

  public TemplateService(UserRepository userRepository, TemplateRepository templateRepository,
      FieldTemplateMapper fieldTemplateMapper, TemplateMapper templateMapper) {
    this.userRepository = userRepository;
    this.templateRepository = templateRepository;
    this.fieldTemplateMapper = fieldTemplateMapper;
    this.templateMapper = templateMapper;
  }

  @Transactional
  public TemplateDTO createTemplate(TemplateDTO templateDTO) {
    Optional<User> owner = userRepository.findByUserId(templateDTO.getOwnerId());

    if (owner.isEmpty()) {
      throw new IllegalArgumentException("User with such id was not found");
    }
    Optional<Template> templateExists = templateRepository.findByNameAndOwner(templateDTO.getName(),
        owner.get());

    if (templateExists.isPresent()) {
      throw new IllegalArgumentException("Template with that name already exists");
    }

    Template template = new Template();
    template.setTemplateId(UUID.randomUUID());
    template.setName(templateDTO.getName());
    template.setOwner(owner.get());

    for (FieldTemplateDTO fieldTemplateDTO : templateDTO.getFields()) {
      FieldTemplate fieldTemplate = fieldTemplateMapper.toEntity(fieldTemplateDTO);
      fieldTemplate.setFieldTemplateId(UUID.randomUUID());
      template.addField(fieldTemplate);
    }

    return templateMapper.toDTO(templateRepository.save(template));
  }

  public TemplateDTO findTemplateById(UUID templateId) {
    Optional<Template> template = templateRepository.findByTemplateId(templateId);
    if (template.isPresent()) {
      return templateMapper.toDTO(template.get());
    } else {
      throw new NoSuchElementException("Template Not found");
    }
  }

  public List<TemplateDTO> findAllOwnerTemplates(UUID ownerID) {
    Optional<User> owner = userRepository.findByUserId(ownerID);

    if (owner.isEmpty()) {
      throw new IllegalArgumentException("User with such id was not found");
    }

    return templateMapper.toDTOList(templateRepository.findAllByOwner(owner.get()));
  }
}
