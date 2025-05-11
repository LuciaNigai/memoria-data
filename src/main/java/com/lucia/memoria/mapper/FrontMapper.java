package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.FrontDTO;
import com.lucia.memoria.model.Front;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = CardMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FrontMapper {
    FrontDTO toDTO(Front front);
    Front toEntity(FrontDTO frontDTO);
    List<FrontDTO> toDTO(List<Front> frontList);
    List<Front> toEntity(List<FrontDTO> frontDTOList);
}
