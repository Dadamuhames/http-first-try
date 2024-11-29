package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.Response;
import org.example.utils.HttpStatusService;
import org.example.utils.HttpVersion;
import org.example.utils.ResponseEntity;

@Slf4j
public class HttpResponseBuilder {

  public static Map<String, String> getHeaders() {
    Map<String, String> headers = new HashMap<>();

    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
    String httpDate = ZonedDateTime.now().format(formatter);

    headers.put("Date", httpDate);
    headers.put("MIME-version", "1.0");

    return headers;
  }

  public Response getResponse(final Integer status, final String body) {
    Map<String, String> headers = getHeaders();

    headers.put("Content-Type", "application/json");
    headers.put("Content-Length", String.valueOf(body.length()));

    return Response.builder()
        .version(HttpVersion.HTTP1)
        .status(status)
        .body(body)
        .headers(headers)
        .build();
  }

  public String serializeResponseBody(final Object body) {
    ObjectMapper objectMapper = new ObjectMapper();
    String jacksonData;

    try {
      jacksonData = objectMapper.writeValueAsString(body);
    } catch (Exception exception) {
      log.error("Cannot serialize response body: {}", exception.getMessage());
      return null;
    }

    return jacksonData;
  }

  public Response getResponse(final Integer status, final Object body) {
    String serializedBody = serializeResponseBody(body);

    return getResponse(status, serializedBody);
  }

  public Response responseEntityToResponse(final ResponseEntity<?> responseEntity) {
    String jacksonData = serializeResponseBody(responseEntity.getBody());

    return getResponse(responseEntity.getStatus(), jacksonData);
  }

  public String responseToString(final Response response) throws Exception {
    StringBuilder stringBuilder = new StringBuilder();

    String statusPhrase = new HttpStatusService().getPhraseForStatus(response.getStatus());

    stringBuilder.append(
        String.format("%s %s %s\r\n", "HTTP/1.0", response.getStatus(), statusPhrase));

    for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
      stringBuilder.append(String.format("%s: %s\r\n", entry.getKey(), entry.getValue()));
    }

    stringBuilder.append("\n");

    stringBuilder.append(response.getBody());

    return stringBuilder.toString();
  }
}
