package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.model.Deck;

import java.util.IdentityHashMap;
import java.util.Map;

public class DeckCycleAvoidingContext {
    private final Map<Deck, DeckDTO> knownInstances = new IdentityHashMap<>();

    public DeckDTO getMappedInstance(Deck source) {
        return knownInstances.get(source);
    }

    public void storeMappedInstance(Deck source, DeckDTO target) {
        knownInstances.put(source, target);
    }
}
