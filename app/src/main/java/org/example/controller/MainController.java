package org.example.controller;

import org.example.HttpResponseBuilder;
import org.example.annotation.Controller;
import org.example.annotation.GetMethod;
import org.example.dto.Request;
import org.example.dto.Response;

import java.util.Map;

@Controller("/main")
public class MainController {

  @GetMethod("/")
  public Response index(final Request request) {
    return null;
  }

  @GetMethod("/news")
  public Response news(final Request request) {
    Map<String, String> responseBody = Map.of("message", "success");

    return HttpResponseBuilder.getResponse(200, responseBody);
  }
}
