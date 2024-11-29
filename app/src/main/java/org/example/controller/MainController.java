package org.example.controller;

import java.util.Map;
import org.example.annotation.Controller;
import org.example.annotation.GetMethod;
import org.example.dto.ArticleDto;
import org.example.dto.Request;
import org.example.dto.Response;
import org.example.utils.ResponseEntity;

@Controller("/main")
public class MainController {

  @GetMethod("/")
  public Response index(final Request request) {
    return null;
  }

  @GetMethod("/news")
  public ResponseEntity<Map<String, String>> news(final Request request) {
    Map<String, String> responseBody = Map.of("message", "success");

    return ResponseEntity.ok(responseBody).status(200);
  }

  @GetMethod("/article")
  public ResponseEntity<ArticleDto> article(final Request request) {
    ArticleDto articleDto = new ArticleDto(1L, "name");

    return ResponseEntity.ok(articleDto);
  }
}
