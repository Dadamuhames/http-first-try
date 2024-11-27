package org.example;

import java.lang.reflect.Method;
import org.example.dto.Request;
import org.example.dto.Response;

public class RequestHandler {
  private final HttpResponseBuilder httpResponseBuilder;

  public RequestHandler(HttpResponseBuilder httpResponseBuilder) {
    this.httpResponseBuilder = httpResponseBuilder;
  }

  public Response getResponse(final Request request) throws Exception {
    String requestKey = String.format("%s[%s]", request.getMethod(), request.getPath());

    Method method = BeanContainer.controllerMethods.get(requestKey);

    Object instance = BeanContainer.controllers.get(method.getDeclaringClass().getName());

    Object response = null;

    try {
      response = method.invoke(instance, request);
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    if (!(response instanceof Response)) {
      throw new Exception("method invalid");
    }

    return (Response) response;
  }

  public String getResponseAsString(final Request request) throws Exception {
    Response response = getResponse(request);

    return httpResponseBuilder.responseToString(response);
  }
}
