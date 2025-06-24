package com.lucia.memoria.controller;

import com.lucia.memoria.dto.externalapi.ResponseDTO;
import com.lucia.memoria.service.external.FreeDictionaryAPIService;
import com.lucia.memoria.service.external.GoogleAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/search")
public class ExternalAPIController {

  private final FreeDictionaryAPIService freeDictionaryAPIService;
  private final GoogleAPIService googleAPIService;

  public ExternalAPIController(FreeDictionaryAPIService freeDictionaryAPIService,
      GoogleAPIService googleAPIService) {
    this.freeDictionaryAPIService = freeDictionaryAPIService;
    this.googleAPIService = googleAPIService;
  }

  @GetMapping("/meaning/{word}")
  public Mono<ResponseEntity<List<ResponseDTO>>> getWordMeaning(@PathVariable String word) {
    return freeDictionaryAPIService.callExternalApi(word)
        .map(ResponseEntity::ok);
  }

  @GetMapping("/translation/{source}/{target}/{word}")
  public Mono<ResponseEntity<String>> getWordMeaning(@PathVariable String source,
      @PathVariable String target, @PathVariable String word) {
    return googleAPIService.callExternalApi(source, target, word)
        .map(ResponseEntity::ok);
  }

}
