package com.giskard.odds.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {
  public InvalidInputException(String message, Throwable e) {
    super(message, e);
  }
}
