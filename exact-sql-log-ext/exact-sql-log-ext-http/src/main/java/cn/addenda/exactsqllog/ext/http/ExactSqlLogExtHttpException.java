package cn.addenda.exactsqllog.ext.http;

import cn.addenda.exactsqllog.common.exception.ExactSqlLogException;

public class ExactSqlLogExtHttpException extends ExactSqlLogException {

  public ExactSqlLogExtHttpException() {
  }

  public ExactSqlLogExtHttpException(String message) {
    super(message);
  }

  public ExactSqlLogExtHttpException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExactSqlLogExtHttpException(Throwable cause) {
    super(cause);
  }

  public ExactSqlLogExtHttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public static void runWithExactSqlLogExtException(TRunnable tRunnable) {
    try {
      tRunnable.run();
    } catch (Throwable t) {
      throw new ExactSqlLogExtHttpException(t);
    }
  }

  public static <T> T getWithExactSqlLogExtException(TSupplier<T> tSupplier) {
    try {
      return tSupplier.get();
    } catch (Throwable t) {
      throw new ExactSqlLogExtHttpException(t);
    }
  }

  @FunctionalInterface
  public interface TRunnable {

    void run() throws Throwable;
  }

  @FunctionalInterface
  public interface TSupplier<T> {

    T get() throws Throwable;
  }

}
