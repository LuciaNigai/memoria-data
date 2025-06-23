package com.lucia.memoria.controller;

import com.lucia.memoria.dto.local.TemplateDTO;
import com.lucia.memoria.service.local.TemplateService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("template")
public class TemplateController {

  private final TemplateService templateService;

  public TemplateController(TemplateService templateService) {
    this.templateService = templateService;
  }


  @PostMapping
  public ResponseEntity<TemplateDTO> saveTemplate(@RequestBody TemplateDTO templateDTO) {
    return ResponseEntity.ok().body(templateService.createTemplate(templateDTO));
  }

  @GetMapping("/{templateId}")
  public ResponseEntity<TemplateDTO> findTemplateById(@PathVariable UUID templateId) {
    return ResponseEntity.ok().body(templateService.findTemplateById(templateId));
  }

  @GetMapping("/all/{ownerId}")
  public ResponseEntity<List<TemplateDTO>> findAllOwnerTemplates(@PathVariable UUID ownerId) {
    return ResponseEntity.ok().body(templateService.findAllOwnerTemplates(ownerId));
  }
}
