package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.dto.local.FieldDTO;
import com.lucia.memoria.dto.local.FieldMinimalDTO;
import com.lucia.memoria.dto.local.ResponseDeckWithCardsDTO;
import com.lucia.memoria.exception.DuplicateException;
import com.lucia.memoria.exception.NotFoundException;
import com.lucia.memoria.helper.FieldRole;
import com.lucia.memoria.helper.FieldType;
import com.lucia.memoria.helper.TemplateFieldType;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
  public CardMinimalDTO createCard(CardMinimalDTO cardDTO, boolean saveDuplicate) {
    Deck deck = deckService.getDeckEntityById(cardDTO.getDeckId());
    Template template = templateService.getTemplateEntityById(cardDTO.getTemplateId());

    Card card = new Card();
    card.setCardId(UUID.randomUUID());
    card.setTemplate(template);

    Optional.ofNullable(cardDTO.getFieldMinimalDTOList())
        .orElse(Collections.emptyList())
        .forEach(minimalDTO -> addCardField(saveDuplicate, minimalDTO, card));

    validateCardFields(card);
    card.setDeck(deck);

    Card savedCard = cardRepository.save(card);

    return cardMapper.toMinimalDTO(savedCard);
  }

  @Transactional(readOnly = true)
  public CardDTO getCardById(UUID cardId) {
    Objects.requireNonNull(cardId, "cardId must not be null");

    Card card = cardRepository.findByCardIdWithFieldsAndFieldTemplates(cardId)
        .orElseThrow(() -> new NotFoundException("Invalid card ID provided."));
    List<TemplateField> templateFields = card.getFields().stream().map(Field::getTemplateField)
        .toList();

    List<FieldDTO> fields = buildFullFields(card, templateFields);
    CardDTO cardDTO = cardMapper.toDTO(card);
    cardDTO.setFieldDTOList(fields);

    return cardDTO;
  }

  @Transactional(readOnly = true)
  public ResponseDeckWithCardsDTO getDeckWithCards(UUID deckId) {
    Deck deck = deckService.getDeckEntityById(deckId);
    List<Card> cards = cardRepository.findAllByDeck(deck);

    return deckWithCardsMapper.toDTO(deck, cards);
  }

  @Transactional
  public void deleteCard(UUID cardId) {
    Card card = cardRepository.findByCardId(cardId)
        .orElseThrow(() -> new NotFoundException("The card not found"));
    cardRepository.delete(card);
  }

  private List<FieldDTO> buildFullFields(Card card, List<TemplateField> templateFields) {
    Map<UUID, Field> cardFieldsByTemplateId = card.getFields().stream()
        .collect(
            Collectors.toMap(f -> f.getTemplateField().getTemplateFieldId(), Function.identity()));

    return templateFields.stream()
        .map(templateField -> convertOrCreateBlankFieldDTO(templateField, cardFieldsByTemplateId))
        .toList();
  }

  private void addCardField(boolean saveDuplicate, FieldMinimalDTO minimalDTO, Card card) {
    TemplateField templateField = templateFieldService.findTemplateFieldById(
        minimalDTO.getTemplateFieldId());

    Field field = new Field();
    field.setFieldId(UUID.randomUUID());

    setCardContent(minimalDTO, templateField.getTemplateFieldType(), field, saveDuplicate);

    field.setTemplateField(templateField);

    card.addField(field);
  }

  private FieldDTO convertOrCreateBlankFieldDTO(TemplateField templateField,
      Map<UUID, Field> cardFieldsByTemplateId) {
    Field field = cardFieldsByTemplateId.get(templateField.getTemplateFieldId());
    return Optional.ofNullable(field)
        .map(f -> {
          FieldDTO fieldDTO = fieldMapper.toDTO(f);
          fieldDTO.setFieldTemplate(templateFieldMapper.toDTO(templateField));
          return fieldDTO;
        })
        .orElse(FieldDTO.blankWithTemplate(templateFieldMapper.toDTO(templateField)));
  }

  private void setCardContent(FieldMinimalDTO minimalDTO,
      TemplateFieldType templateFieldType,
      Field field, boolean saveDuplicate) {

    if (templateFieldType == null) {
      throw new IllegalArgumentException("TemplateFieldType must not be null");
    }

    FieldType fieldType = templateFieldType.getFieldType();

    if (fieldType == FieldType.ENUM || fieldType == FieldType.MULTI_TAG) {
      if (templateFieldType.getOptions() != null
          && templateFieldType.getOptions().contains(minimalDTO.getContent())) {
        field.setContent(minimalDTO.getContent());
      } else {
        throw new IllegalArgumentException(
            "The option you choose is not valid. Please choose one of the options: "
                + templateFieldType.getOptions());
      }
      return;
    }
    List<Card> duplicates = cardRepository.findByFieldContentWithFields(minimalDTO.getContent());
    if (!duplicates.isEmpty() && !saveDuplicate) {
      throw new DuplicateException(
          "The card with such field " + minimalDTO.getContent()
              + " already exists. Are you sure you want to save it?",
          new ArrayList<>(cardMapper.toMinimalDTOList(duplicates))
      );
    }

    field.setContent(minimalDTO.getContent());
  }

  private void validateCardFields(Card card) {
    Set<FieldRole> roles = card.getFields().stream()
        .map(Field::getTemplateField)
        .map(TemplateField::getFieldRole)
        .collect(Collectors.toSet());

    if (!roles.contains(FieldRole.FRONT) || !roles.contains(FieldRole.BACK)) {
      throw new IllegalArgumentException("Card must have at least one FRONT and one BACK field");
    }
  }
}
