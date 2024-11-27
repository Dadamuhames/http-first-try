package org.example;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.dto.Request;
import org.example.utils.HttpVersion;
import org.example.utils.Methods;

public class HttpRequestParser {

  public Methods parseMethod(final String data) throws Exception {
    try {
      return Methods.valueOf(data);
    } catch (IllegalArgumentException e) {
      // replace with custom http exception
      throw new Exception("501 Method not implemented");
    }
  }

  public HttpVersion parseVersion(final String data) throws Exception {
    return switch (data) {
      case "HTTP/1.0" -> HttpVersion.HTTP1;

      case "HTTP/0.9" -> HttpVersion.HTTP09;

      default -> throw new Exception("Version invalid");
    };
  }

  public void parsePath(final String path, final Request request) {
    if (path.isEmpty() || (int) path.toCharArray()[0] == 0) {
      request.setPath("/");
      request.setFullPath("/");
      return;
    }

    request.setFullPath(path);
    request.setPath(path);

    if (path.contains("?")) {
      String[] splitPath = path.split("\\?");

      request.setPath(splitPath[0]);

      if (splitPath.length == 2) {
        parseQueryParams(splitPath[1], request);
      }
    }
  }

  public void parseRequestLine(final String data, final Request request) throws Exception {
    List<String> dataList = Arrays.stream(data.split(" ")).toList();

    if (dataList.size() != 3) throw new Exception("Request line invalid");

    // get method
    Methods httpMethod = parseMethod(dataList.getFirst());
    request.setMethod(httpMethod);

    // get path
    parsePath(dataList.get(1), request);

    // parse version
    HttpVersion version = parseVersion(dataList.getLast());
    request.setVersion(version);
  }

  public void parseQueryParams(final String queryString, final Request request) {
    if (queryString.isEmpty()) return;

    Map<String, Object> params = new HashMap<>();

    String[] queryParamsList = queryString.split("&");

    for (String param : queryParamsList) {
      String[] paramAsList = param.split("=");

      if (paramAsList.length >= 2) {
        params.put(paramAsList[0], paramAsList[1]);
      } else {
        params.put(param.replace("=", ""), null);
      }
    }

    request.setParams(params);
  }

  public void parseHeaders(final String headerLine, final Request request) {
    Map<String, String> headers =
        request.getHeaders() != null ? request.getHeaders() : new HashMap<>();

    if (headerLine.matches("([\\w-]+): (.*)")) {
      String[] headerLineAsList = headerLine.split(":");
      headers.put(headerLineAsList[0].trim(), headerLineAsList[1].trim());
      request.setHeaders(headers);
    }
  }

  public void parseBody() {}

  public Request parse(final ByteBuffer buffer) throws Exception {
    ByteArrayInputStream in = new ByteArrayInputStream(buffer.array());

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

    Request request = new Request();

    String line;
    int lineNumber = 1;

    while ((line = bufferedReader.readLine()) != null) {
      if (line.isEmpty()) {
        lineNumber++;
        continue;
      }

      switch (lineNumber) {
        case 1 -> {
          parseRequestLine(line, request);
          lineNumber++;
        }

        case 2 -> parseHeaders(line, request);
      }
    }

    return request;
  }
}
