package com.xtremelabs.xtremeutil.net.api.exception;

public class RequestFailedToStartException extends Exception {
  private Throwable cause;

  public RequestFailedToStartException(String message) {
    super(message);
  }

  public RequestFailedToStartException(Throwable t) {
    super(t.getMessage());
    this.cause = t;
  }

  public Throwable getCause() {
    return this.cause;
  }
}
