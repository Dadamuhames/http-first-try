package org.example.factory;

import org.example.HttpResponseBuilder;
import org.example.RequestHandler;
import org.example.RequestParser;
import org.example.service.ResponseService;

public class RequestHandlerFactory {
  public static RequestHandler createRequestHandler() {
    HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();

    ResponseService responseService = ResponseServiceFactory.createResponseService();

    RequestParser requestParser = new RequestParser();

    return new RequestHandler(httpResponseBuilder, responseService, requestParser);
  }
}
