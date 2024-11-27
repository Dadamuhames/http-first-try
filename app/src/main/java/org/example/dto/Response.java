package org.example.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.utils.HttpVersion;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
  private HttpVersion version;

  private Integer status;

  private Map<String, String> headers;

  private String body;
}
