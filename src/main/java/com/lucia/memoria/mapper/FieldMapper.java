package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.FieldDTO;
import com.lucia.memoria.dto.local.FieldMinimalDTO;
import com.lucia.memoria.model.Field;
import com.lucia.memoria.model.FieldTemplate;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = FieldTemplateMapper.class)
public interface FieldMapper {
    @Mapping(target = "fieldId", source = "fieldId")
    FieldDTO toDTO(Field field);
    @Mapping(target = "fieldTemplateId", source = "fieldTemplate", qualifiedByName = "fieldTemplateToFieldTemplateId")
    FieldMinimalDTO toMinimalDTO(Field field);
    @Mapping(target = "fieldTemplateId", source = "fieldTemplate", qualifiedByName = "fieldTemplateToFieldTemplateId")
    List<FieldMinimalDTO> toMinimalDTOList(List<Field> fields);

    Field toEntity(FieldDTO fieldDTO);

    @Named("fieldTemplateToFieldTemplateId")
    default UUID mapFieldTemplateToFieldTemplateId(FieldTemplate fieldTemplate) {
        return fieldTemplate == null ? null : fieldTemplate.getFieldTemplateId();
    }
}
