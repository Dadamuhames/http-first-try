package org.example.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEntity<T> {
  T body;
  Integer status;

  protected void setStatus(final Integer status) {
    this.status = status;
  }

  public static <T> ResponseEntity<T> create(final T body, final Integer status) {
    return new ResponseEntity<T>(body, status);
  }

  public static <T> ResponseEntity<T> ok() {
    ResponseEntity<T> responseEntity = new ResponseEntity<T>();

    responseEntity.setStatus(200);

    return responseEntity;
  }

  public static <T> ResponseEntity<T> ok(final T body) {
    return new ResponseEntity<T>(body, 200);
  }

  public ResponseEntity<T> status(final Integer status) {
    setStatus(status);
    return this;
  }
}
