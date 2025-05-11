package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.model.Deck;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CardMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeckMapper {

    @Mapping(target = "parentDeck", source = "parentDeck", qualifiedByName = "mapParentDeck")
    DeckDTO toDTO(Deck deck, @Context DeckCycleAvoidingContext context);

    List<DeckDTO> toDTO(List<Deck> decks, @Context DeckCycleAvoidingContext context);

    List<Deck> toEntity(List<DeckDTO> deckDTOList, @Context DeckCycleAvoidingContext context);

    default DeckDTO toDTO(Deck deck) {
        return toDTO(deck, new DeckCycleAvoidingContext());
    }

    default List<DeckDTO> toDTO(List<Deck> decks) {
        return toDTO(decks, new DeckCycleAvoidingContext());
    }

    @Named("mapParentDeck")
    default DeckDTO mapParentDeck(Deck deck, @Context DeckCycleAvoidingContext context) {
        return toDTO(deck, context);
    }
}
