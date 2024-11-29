package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.example.dto.Request;
import org.example.dto.Response;
import org.example.service.ResponseService;

public class RequestHandler {
  private final HttpResponseBuilder httpResponseBuilder;
  private final ResponseService responseService;
  private final RequestParser requestParser;

  public RequestHandler(
      HttpResponseBuilder httpResponseBuilder,
      ResponseService responseService,
      RequestParser requestParser) {
    this.httpResponseBuilder = httpResponseBuilder;
    this.responseService = responseService;
    this.requestParser = requestParser;
  }

  public void handleRequest(final SelectionKey key) throws Exception {
    SocketChannel client = (SocketChannel) key.channel();

    ByteBuffer buffer = ByteBuffer.allocate(1024);

    client.read(buffer);

    byte[] bufferByteArray = buffer.array();

    if (bufferByteArray.length == 0 || bufferByteArray[0] == 0) {
      key.cancel();
      return;
    }

    // parsing data to request entity
    Request request = requestParser.parse(buffer);

    // getting response depending on data of request
    Response response = responseService.getResponse(request);

    // serialize response to string
    String responseString = httpResponseBuilder.responseToString(response);

    sendResponse(client, responseString);
  }

  public void sendResponse(final SocketChannel client, final String data) throws IOException {
    ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());

    while (buffer.hasRemaining()) {
      client.write(buffer);
    }

    client.finishConnect();
    client.close();
  }
}
