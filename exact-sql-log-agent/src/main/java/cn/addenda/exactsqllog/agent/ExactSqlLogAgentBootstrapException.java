package cn.addenda.exactsqllog.agent;

public class ExactSqlLogAgentBootstrapException extends RuntimeException {

  public ExactSqlLogAgentBootstrapException() {
  }

  public ExactSqlLogAgentBootstrapException(String message) {
    super(message);
  }

  public ExactSqlLogAgentBootstrapException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExactSqlLogAgentBootstrapException(Throwable cause) {
    super(cause);
  }

  public ExactSqlLogAgentBootstrapException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
