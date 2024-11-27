package org.example.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpStatusService {
  public static final Map<Integer, String> statusPhrases = new HashMap<>();

  public HttpStatusService() {
    statusPhrases.put(200, "OK");
    statusPhrases.put(201, "Created");
    statusPhrases.put(202, "Accepted");
    statusPhrases.put(204, "No Content");
    statusPhrases.put(301, "Moved Permanently");
    statusPhrases.put(302, "Moved Temporarily");
    statusPhrases.put(304, "Not Modified");
    statusPhrases.put(400, "Bad Request");
    statusPhrases.put(401, "Unauthorized");
    statusPhrases.put(403, "Forbidden");
    statusPhrases.put(404, "Not Found");
    statusPhrases.put(500, "Internal Server Error");
    statusPhrases.put(501, "Not Implemented");
    statusPhrases.put(502, "Bad Gateway");
    statusPhrases.put(503, "Service Unavailable");
  }

  public String getPhraseForStatus(final Integer status) throws Exception {
    String phrase = statusPhrases.get(status);

    if (phrase == null) {
      throw new Exception("Phrase not found");
    }

    return phrase;
  }
}
