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
import com.lucia.memoria.mapper.TemplateFieldMapper;
import com.lucia.memoria.model.Card;
import com.lucia.memoria.model.Deck;
import com.lucia.memoria.model.Field;
import com.lucia.memoria.model.TemplateField;
import com.lucia.memoria.model.Template;
import com.lucia.memoria.repository.CardRepository;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CardService {

  private final CardRepository cardRepository;
  private final DeckService deckService;
  private final TemplateService templateService;
  private final TemplateFieldService templateFieldService;
  private final CardMapper cardMapper;
  private final FieldMapper fieldMapper;
  private final TemplateFieldMapper templateFieldMapper;
  private final DeckWithCardsMapper deckWithCardsMapper;


  public CardService(CardRepository cardRepository, DeckService deckService,
      TemplateService templateService, TemplateFieldService templateFieldService,
      CardMapper cardMapper, FieldMapper fieldMapper, TemplateFieldMapper templateFieldMapper,
      DeckWithCardsMapper deckWithCardsMapper) {
    this.cardRepository = cardRepository;
    this.deckService = deckService;
    this.templateService = templateService;
    this.templateFieldService = templateFieldService;
    this.cardMapper = cardMapper;
    this.fieldMapper = fieldMapper;
    this.templateFieldMapper = templateFieldMapper;
    this.deckWithCardsMapper = deckWithCardsMapper;
  }

  @Transactional
  public CardMinimalDTO saveCard(CardMinimalDTO cardDTO) {
    Deck deck = deckService.findByDeckId(cardDTO.getDeckId());
    Template template = templateService.findTemplateEntityById(cardDTO.getTemplateId());

    Card card = new Card();
    card.setCardId(UUID.randomUUID());
    card.setTemplate(template);

    for (FieldMinimalDTO minimalDTO : cardDTO.getFieldMinimalDTOList()) {
      TemplateField templateField = templateFieldService.findByFieldTemplateIdAndTemplate(
          minimalDTO.getTemplateFieldId(), template);

      Field field = new Field();
      field.setFieldId(UUID.randomUUID());
      field.setContent(minimalDTO.getContent());
      field.setTemplateField(templateField);

      card.addField(field);
    }

    validateCardFields(card);

    card.setDeck(deck);

    return cardMapper.toMinimalDTO(cardRepository.save(card));
  }

  @Transactional(readOnly = true)
  public ResponseDeckWithCardsDTO getDeckWithFlashCards(String deckPath) {
    Deck deck = deckService.findByPath(deckPath);
    List<Card> cards = cardRepository.findAllByDeck(deck);

    return deckWithCardsMapper.toDTO(deck, cards);
  }

  @Transactional(readOnly = true)
  public CardDTO getCardById(UUID cardId) {
    Card card = cardRepository.findByCardIdWithFields(cardId)
        .orElseThrow(() -> new NotFoundException("Card not found"));
    List<TemplateField> templateFields = templateService.findTemplateWithFields(
        card.getTemplate().getTemplateId()).getFields();

    List<FieldDTO> fields = buildFullFields(card, templateFields);
    CardDTO cardDTO = cardMapper.toDTO(card);
    cardDTO.setFieldDTOList(fields);
    return cardDTO;
  }


  private List<FieldDTO> buildFullFields(Card card, List<TemplateField> templateFields) {

    Map<UUID, Field> cardFieldsByTemplateId = card.getFields().stream()
        .collect(
            Collectors.toMap(f -> f.getTemplateField().getFieldTemplateId(), Function.identity()));

    return templateFields.stream()
        .map(templateField -> {
          Field field = cardFieldsByTemplateId.get(templateField.getFieldTemplateId());
          if (field != null) {
            FieldDTO fieldDTO = fieldMapper.toDTO(field);
            fieldDTO.setFieldTemplate(templateFieldMapper.toDTO(templateField));
            return fieldDTO;
          } else {
            FieldDTO blank = new FieldDTO();
            blank.setContent(null);
            blank.setFieldTemplate(templateFieldMapper.toDTO(templateField));
            return blank;
          }
        })
        .toList();
  }


  private void validateCardFields(Card card) {
    boolean hasFront = card.getFields().stream()
        .map(Field::getTemplateField)
        .anyMatch(ft -> ft.getFieldRole() == FieldRole.FRONT);

    boolean hasBack = card.getFields().stream()
        .map(Field::getTemplateField)
        .anyMatch(ft -> ft.getFieldRole() == FieldRole.BACK);
    if (!hasFront || !hasBack) {
      throw new IllegalArgumentException("Card must have at least one FRONT and one BACK field");
    }
  }
}
