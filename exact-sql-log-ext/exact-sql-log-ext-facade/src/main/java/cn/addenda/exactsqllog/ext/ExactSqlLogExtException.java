package cn.addenda.exactsqllog.ext;

import cn.addenda.exactsqllog.common.exception.ExactSqlLogException;

public class ExactSqlLogExtException extends ExactSqlLogException {

  public ExactSqlLogExtException() {
  }

  public ExactSqlLogExtException(String message) {
    super(message);
  }

  public ExactSqlLogExtException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExactSqlLogExtException(Throwable cause) {
    super(cause);
  }

  public ExactSqlLogExtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public static void runWithExactSqlLogExtException(TRunnable tRunnable) {
    try {
      tRunnable.run();
    } catch (Throwable t) {
      throw new ExactSqlLogExtException(t);
    }
  }

  public static <T> T getWithExactSqlLogExtException(TSupplier<T> tSupplier) {
    try {
      return tSupplier.get();
    } catch (Throwable t) {
      throw new ExactSqlLogExtException(t);
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
