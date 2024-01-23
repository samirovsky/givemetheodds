package com.giskard.odds.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CannotCalculateOddsException extends RuntimeException {
  public CannotCalculateOddsException(String message) {
    super(message);
  }

  public CannotCalculateOddsException(Throwable e) {
    super(e);
  }

  public CannotCalculateOddsException(String message, Throwable e) {
    super(message, e);
  }
}
