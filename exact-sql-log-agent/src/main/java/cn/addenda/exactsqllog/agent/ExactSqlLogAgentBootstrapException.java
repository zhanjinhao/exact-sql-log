package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.common.exception.ExactSqlLogException;

public class ExactSqlLogAgentBootstrapException extends ExactSqlLogException {

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
