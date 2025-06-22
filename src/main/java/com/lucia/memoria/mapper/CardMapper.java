package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.model.Card;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card toEntity(CardDTO cardDTO);
    @Mapping(target = "cardId", source = "cardId")
    CardMinimalDTO toMinimalDTO(Card card);

    @AfterMapping
    default void setCardID(@MappingTarget Card card) {
        if(card.getCardId() == null ) {
            card.setCardId(UUID.randomUUID());
        }
    }
}
