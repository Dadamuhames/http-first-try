package org.example.service;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.example.BeanContainer;
import org.example.HttpResponseBuilder;
import org.example.dto.Request;
import org.example.dto.Response;
import org.example.utils.ResponseEntity;

@Slf4j
public class ResponseService {
  private final HttpResponseBuilder httpResponseBuilder;

  public ResponseService(HttpResponseBuilder httpResponseBuilder) {
    this.httpResponseBuilder = httpResponseBuilder;
  }

  // getting response depending on HTTP Method and path
  public Response getResponse(final Request request) {
    String requestKey = String.format("%s[%s]", request.getMethod(), request.getPath());

    Method method = BeanContainer.controllerMethods.get(requestKey);

    Object instance = BeanContainer.controllers.get(method.getDeclaringClass().getName());

    Object responseBody = null;

    try {
      responseBody = method.invoke(instance, request);
    } catch (Exception exception) {
      log.error("Cannot invoke method of controller: {}", exception.getMessage());
    }

    if (responseBody instanceof ResponseEntity<?>) {
      return httpResponseBuilder.responseEntityToResponse((ResponseEntity<?>) responseBody);
    }

    return httpResponseBuilder.getResponse(200, responseBody);
  }
}
