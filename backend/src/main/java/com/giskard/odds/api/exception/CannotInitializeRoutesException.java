package com.giskard.odds.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotInitializeRoutesException extends RuntimeException {

  public CannotInitializeRoutesException(String message) {
    super(message);
  }
}
