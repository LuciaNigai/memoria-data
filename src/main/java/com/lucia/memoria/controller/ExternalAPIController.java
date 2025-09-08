package com.lucia.memoria.controller;

import com.lucia.memoria.dto.externalapi.ResponseDTO;
import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.service.external.FreeDictionaryAPIService;
import com.lucia.memoria.service.external.FreeDictionaryCardService;
import com.lucia.memoria.service.external.GoogleAPIService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/data/search")
@AllArgsConstructor
public class ExternalAPIController {

  private final FreeDictionaryCardService freeDictionaryCardService;
  private final FreeDictionaryAPIService freeDictionaryAPIService;
  private final GoogleAPIService googleAPIService;


  @GetMapping("/meaning/{word}")
  public Mono<List<CardDTO>> getWordMeaning(@PathVariable("word") String word) {
    return freeDictionaryCardService.generateCards(word);
  }

  // TODO: remove later
  @GetMapping("/meaning/{word}/old")
  public Mono<ResponseEntity<List<ResponseDTO>>> getWordMeaningOld(@PathVariable("word") String word) {
    return freeDictionaryAPIService.callExternalApi(word)
        .map(ResponseEntity::ok);
  }

  @GetMapping("/translation/{source}/{target}/{word}")
  public Mono<String> getWordMeaning(@PathVariable("source") String source,
      @PathVariable("target") String target, @PathVariable("word") String word) {
    return googleAPIService.callExternalApi(source, target, word);
  }

}
