package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.common.exception.ExactSqlLogException;

public class ExactSqlLogAgentStartException extends ExactSqlLogException {

  public ExactSqlLogAgentStartException() {
  }

  public ExactSqlLogAgentStartException(String message) {
    super(message);
  }

  public ExactSqlLogAgentStartException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExactSqlLogAgentStartException(Throwable cause) {
    super(cause);
  }

  public ExactSqlLogAgentStartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
