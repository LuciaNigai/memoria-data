package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.CardTypeDTO;
import com.lucia.memoria.model.CardType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardTypeMapper {
    CardTypeDTO toDTO(CardType cardType);
    CardType toEntity(CardTypeDTO cardTypeDTO);
    List<CardTypeDTO> toDTO(List<CardType> cardTypeList);
    List<CardType> toEntity(List<CardType> cardTypeList);
}
