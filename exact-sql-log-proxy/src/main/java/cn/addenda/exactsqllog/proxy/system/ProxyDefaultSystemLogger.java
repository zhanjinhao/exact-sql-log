package cn.addenda.exactsqllog.proxy.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyDefaultSystemLogger implements SystemLogger {

  private final Logger log;

  public ProxyDefaultSystemLogger(String name) {
    this.log = LoggerFactory.getLogger(name);
  }

  @Override
  public void trace(String msg) {
    log.trace(msg);
  }

  @Override
  public void trace(String format, Object... arguments) {
    log.trace(format, arguments);
  }

  @Override
  public void debug(String msg) {
    log.debug(msg);
  }

  @Override
  public void debug(String format, Object... arguments) {
    log.debug(format, arguments);
  }

  @Override
  public void info(String msg) {
    log.info(msg);
  }

  @Override
  public void info(String format, Object... arguments) {
    log.info(format, arguments);
  }

  @Override
  public void warn(String msg) {
    log.warn(msg);
  }

  @Override
  public void warn(String format, Object... arguments) {
    log.warn(format, arguments);
  }

  @Override
  public void error(String msg) {
    log.error(msg);
  }

  @Override
  public void error(String format, Object... arguments) {
    log.error(format, arguments);
  }

}
