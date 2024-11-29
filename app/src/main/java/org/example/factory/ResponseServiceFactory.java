package org.example.factory;

import org.example.HttpResponseBuilder;
import org.example.service.ResponseService;

public class ResponseServiceFactory {
    public static ResponseService createResponseService() {
        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();

        return new ResponseService(httpResponseBuilder);
    }
}
