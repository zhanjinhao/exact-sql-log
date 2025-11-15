package cn.addenda.exactsqllog.common.exception;

public class ExactSqlLogException extends RuntimeException {

  public ExactSqlLogException() {
  }

  public ExactSqlLogException(String message) {
    super(message);
  }

  public ExactSqlLogException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExactSqlLogException(Throwable cause) {
    super(cause);
  }

  public ExactSqlLogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
