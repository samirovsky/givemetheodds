package com.giskard.odds.api.exception;

public class CannotCalculateOddsCheckedException extends Exception {
  public CannotCalculateOddsCheckedException(String message) {
    super(message);
  }

  public CannotCalculateOddsCheckedException(String message, Throwable e) {
    super(message, e);
  }
}
