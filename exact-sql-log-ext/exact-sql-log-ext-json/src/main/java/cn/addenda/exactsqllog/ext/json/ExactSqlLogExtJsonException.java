package cn.addenda.exactsqllog.ext.json;

import cn.addenda.exactsqllog.common.exception.ExactSqlLogException;

public class ExactSqlLogExtJsonException extends ExactSqlLogException {

  public ExactSqlLogExtJsonException() {
  }

  public ExactSqlLogExtJsonException(String message) {
    super(message);
  }

  public ExactSqlLogExtJsonException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExactSqlLogExtJsonException(Throwable cause) {
    super(cause);
  }

  public ExactSqlLogExtJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public static void runWithExactSqlLogExtException(TRunnable tRunnable) {
    try {
      tRunnable.run();
    } catch (Throwable t) {
      throw new ExactSqlLogExtJsonException(t);
    }
  }

  public static <T> T getWithExactSqlLogExtException(TSupplier<T> tSupplier) {
    try {
      return tSupplier.get();
    } catch (Throwable t) {
      throw new ExactSqlLogExtJsonException(t);
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
