package org.example.factory;

import lombok.extern.slf4j.Slf4j;
import org.example.RequestHandler;
import org.example.TCPServer;

@Slf4j
public class TCPServerFactory {
  public static TCPServer createTCPServer() {
    RequestHandler requestHandler = RequestHandlerFactory.createRequestHandler();

    return new TCPServer(requestHandler);
  }
}
