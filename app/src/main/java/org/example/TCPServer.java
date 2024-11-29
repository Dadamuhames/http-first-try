package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TCPServer {
  private final RequestHandler requestHandler;

  public TCPServer(RequestHandler requestHandler) {
    this.requestHandler = requestHandler;
  }

  public void handleAccept(final Selector selector, final ServerSocketChannel channel)
      throws IOException {
    SocketChannel client = channel.accept();

    client.configureBlocking(false);

    client.register(selector, SelectionKey.OP_READ);
  }

  public void startServer() {
    try {
      Selector selector = Selector.open();

      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

      ServerSocket serverSocket = serverSocketChannel.socket();

      // TODO: HOST and PORT from .env
      serverSocket.bind(new InetSocketAddress("localhost", 7070));

      serverSocketChannel.configureBlocking(false);

      int ops = serverSocketChannel.validOps();

      serverSocketChannel.register(selector, ops, null);

      while (true) {
        selector.select(1000);

        Set<SelectionKey> selectedKeys = selector.selectedKeys();

        Iterator<SelectionKey> i = selectedKeys.iterator();

        while (i.hasNext()) {
          SelectionKey key = i.next();

          if (key.isAcceptable()) {
            handleAccept(selector, serverSocketChannel);
          } else if (key.isReadable() && key.isValid()) {
            try {
              requestHandler.handleRequest(key);
            } catch (Exception exception) {
              log.error("Client error: {}", exception.getMessage());
              key.cancel();
            }
          }

          i.remove();
        }
      }

    } catch (IOException e) {
      log.error("Server error: {}", e.getMessage());
    }
  }
}
