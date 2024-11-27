package org.example.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.HttpVersion;
import org.example.utils.Methods;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
  private Methods method;

  private String path;

  private String fullPath;

  private HttpVersion version;

  private Map<String, String> headers;

  private Map<String, Object> params;

  private String body;
}
