package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CardTypeMapper.class, FrontMapper.class, BackMapper.class, DeckMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {
    CardDTO toDTO(Card card);
    Card toEntity(CardDTO cardDTO);
    List<CardDTO> toDTO(List<Card> cardList);
    List<Card> toEntity(List<CardDTO> cardDTOList);
}
