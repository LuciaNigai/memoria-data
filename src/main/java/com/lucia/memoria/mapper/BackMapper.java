package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.BackDTO;
import com.lucia.memoria.model.Back;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = CardMapper.class,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BackMapper {
    BackDTO toDTO(Back back);
    Back toEntity(BackDTO backDTO);
    List<BackDTO> toDTO(List<Back> backList);
    List<Back> toEntity(List<BackDTO> backDTOList);
}
