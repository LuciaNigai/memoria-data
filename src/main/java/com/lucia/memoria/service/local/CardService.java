package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.dto.local.FieldDTO;
import com.lucia.memoria.dto.local.FieldMinimalDTO;
import com.lucia.memoria.dto.local.ResponseDeckWithCardsDTO;
import com.lucia.memoria.exception.NotFoundException;
import com.lucia.memoria.helper.FieldRole;
import com.lucia.memoria.mapper.CardMapper;
import com.lucia.memoria.mapper.DeckWithCardsMapper;
import com.lucia.memoria.mapper.FieldMapper;
import com.lucia.memoria.mapper.FieldTemplateMapper;
import com.lucia.memoria.model.Card;
import com.lucia.memoria.model.Deck;
import com.lucia.memoria.model.Field;
import com.lucia.memoria.model.FieldTemplate;
import com.lucia.memoria.model.Template;
import com.lucia.memoria.repository.CardRepository;
import com.lucia.memoria.repository.DeckRepository;
import com.lucia.memoria.repository.FieldTemplateRepository;
import com.lucia.memoria.repository.TemplateRepository;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CardService {

  private final DeckRepository deckRepository;
  private final FieldTemplateRepository fieldTemplateRepository;
  private final CardMapper cardMapper;
  private final FieldMapper fieldMapper;
  private final FieldTemplateMapper fieldTemplateMapper;
  private final DeckWithCardsMapper deckWithCardsMapper;
  private final CardRepository cardRepository;
  private final TemplateRepository templateRepository;

  public CardService(DeckRepository deckRepository,
      FieldTemplateRepository fieldTemplateRepository, CardMapper cardMapper,
      FieldMapper fieldMapper, FieldTemplateMapper fieldTemplateMapper,
      DeckWithCardsMapper deckWithCardsMapper, CardRepository cardRepository,
      TemplateRepository templateRepository) {
    this.deckRepository = deckRepository;
    this.fieldTemplateRepository = fieldTemplateRepository;
    this.cardMapper = cardMapper;
    this.fieldMapper = fieldMapper;
    this.fieldTemplateMapper = fieldTemplateMapper;
    this.deckWithCardsMapper = deckWithCardsMapper;
    this.cardRepository = cardRepository;
    this.templateRepository = templateRepository;
  }

  @Transactional
  public CardMinimalDTO saveCard(CardMinimalDTO cardDTO) {
    Deck deck = deckRepository.findByDeckId(cardDTO.getDeckId())
        .orElseThrow(() -> new IllegalArgumentException("Wrong deck id"));

    Template template = templateRepository.findByTemplateId(cardDTO.getTemplateId())
        .orElseThrow(() -> new IllegalArgumentException("Wrong template id"));

    Card card = new Card();
    card.setCardId(UUID.randomUUID());
    card.setTemplate(template);

    for (FieldMinimalDTO minimalDTO : cardDTO.getFieldMinimalDTOList()) {
      FieldTemplate fieldTemplate = fieldTemplateRepository.findByFieldTemplateIdAndTemplate(
              minimalDTO.getFieldTemplateId(), template)
          .orElseThrow(() -> new IllegalArgumentException("Target template field does not exists"));

      Field field = new Field();
      field.setFieldId(UUID.randomUUID());
      field.setContent(minimalDTO.getContent());
      field.setFieldTemplate(fieldTemplate);

      card.addField(field);
    }

    validateCardFields(card);

    card.setDeck(deck);

    return cardMapper.toMinimalDTO(cardRepository.save(card));
  }

  @Transactional
  public ResponseDeckWithCardsDTO getDeckWithFlashCards(String deckPath) {
    Deck deck = deckRepository.findByPath(deckPath)
        .orElseThrow(() -> new IllegalArgumentException("Deck not found: " + deckPath));

    List<Card> cards = cardRepository.findAllByDeck(deck);

    return deckWithCardsMapper.toDTO(deck, cards);
  }

  @Transactional
  public CardDTO getCardById(UUID cardId) {
    Card card = cardRepository.findByCardIdWithFieldsAndTemplateFields(cardId)
        .orElseThrow(() -> new NotFoundException("Card not found"));
    Set<FieldDTO> fields = buildFullFields(card);
    CardDTO cardDTO = cardMapper.toDTO(card);
    cardDTO.setFieldDTOList(fields);
    return cardDTO;
  }

  private Set<FieldDTO> buildFullFields(Card card) {
    Map<UUID, Field> cardFieldsByTemplateId = card.getFields().stream()
        .collect(Collectors.toMap(f -> f.getFieldTemplate().getFieldTemplateId(), Function.identity()));

    return card.getTemplate().getFields().stream()
        .map(templateField -> {
          Field field = cardFieldsByTemplateId.get(templateField.getFieldTemplateId());
          if(field != null) {
            return fieldMapper.toDTO(field);
          } else {
            FieldDTO blank = new FieldDTO();
            blank.setContent(null);
            blank.setFieldTemplate(fieldTemplateMapper.toDTO(templateField));
            return blank;
          }
        })
        .collect(Collectors.toSet());
  }


  private void validateCardFields(Card card) {
    boolean hasFront = card.getFields().stream()
        .map(Field::getFieldTemplate)
        .anyMatch(ft -> ft.getFieldRole() == FieldRole.FRONT);

    boolean hasBack = card.getFields().stream()
        .map(Field::getFieldTemplate)
        .anyMatch(ft -> ft.getFieldRole() == FieldRole.BACK);
    if (!hasFront || !hasBack) {
      throw new IllegalArgumentException("Card must have at least one FRONT and one BACK field");
    }
  }
}
