package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import org.example.dto.Request;

public class TCPServer {
  private final Selector selector;
  private final HttpRequestParser httpRequestParser;
  private final RequestHandler requestHandler;

  public TCPServer(
      Selector selector, HttpRequestParser httpRequestParser, RequestHandler requestHandler) {
    this.selector = selector;
    this.httpRequestParser = httpRequestParser;
    this.requestHandler = requestHandler;
  }

  public void handleAccept(final ServerSocketChannel channel, final SelectionKey key)
      throws IOException {

    SocketChannel client = channel.accept();

    client.configureBlocking(false);

    client.register(selector, SelectionKey.OP_READ);
  }

  public String getBinString(final Integer data) {
    String binString = Integer.toBinaryString(data);

    if (binString.length() != 8) {
      binString = "0".repeat(8 - binString.length()) + binString;
    }

    return binString;
  }

  public void handleRead(final SelectionKey key) throws Exception {
    SocketChannel client = (SocketChannel) key.channel();

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    client.read(buffer);

    byte[] bufferByteArray = buffer.array();

    if (bufferByteArray.length == 0 || bufferByteArray[0] == 0) {
      key.cancel();
      return;
    }
    Request request = httpRequestParser.parse(buffer);

    String response = requestHandler.getResponseAsString(request);

    sendResponse(key, response);
  }

  public void sendResponse(final SelectionKey key, final String data) throws IOException {
    SocketChannel client = (SocketChannel) key.channel();

    ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());

    while (buffer.hasRemaining()) {
      client.write(buffer);
    }

    client.finishConnect();
    client.close();
  }

  public void startServer() throws Exception {
    try {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

      ServerSocket serverSocket = serverSocketChannel.socket();

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
            handleAccept(serverSocketChannel, key);
          } else if (key.isReadable() && key.isValid()) {
            try {
              handleRead(key);
            } catch (Exception exception) {
              key.cancel();
            }
          }

          i.remove();
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
