package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.factory.TCPServerFactory;

@Slf4j
public class App {
  public static void main(String[] args) {
    try {
      ControllersLoader controllersLoader = new ControllersLoader();

      controllersLoader.loadControllers();

      TCPServer tcpServer = TCPServerFactory.createTCPServer();

      tcpServer.startServer();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      log.error("Error: {}", e.getMessage());
    }
  }
}
