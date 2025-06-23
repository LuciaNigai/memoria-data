package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.FieldTemplateDTO;
import com.lucia.memoria.model.FieldTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FieldTemplateMapper {
    @Mapping(target = "fieldTemplateId", source = "fieldTemplateId")
    FieldTemplateDTO toDTO(FieldTemplate fieldTemplate);
    FieldTemplate toEntity(FieldTemplateDTO fieldTemplateDTO);
}
