package cn.addenda.exactsqllog.ext.log;

import cn.addenda.exactsqllog.facade.LogFacade;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.util.Properties;

public class EslDefaultLogImpl implements LogFacade {

  private final String name;
  private final String fqcn;
  private final Logger logger;

  public EslDefaultLogImpl(String name, String fqcn) {
    this.name = name;
    this.fqcn = fqcn;
    Properties logProperties = LogConfigAware.getLogProperties();
    String absolutePath = logProperties.getProperty("log4j2.conf.absolutePath");
    this.logger = LoggerContext.getContext(null, false, new File(absolutePath).toURI()).getLogger(name);
  }

  public EslDefaultLogImpl(String name) {
    this(name, EslDefaultLogImpl.class.getName());
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  public void trace(String format) {
    logger.logIfEnabled(fqcn, Level.TRACE, null, format);
  }

  @Override
  public void trace(String format, Object arg) {
    logger.logIfEnabled(fqcn, Level.TRACE, null, format, arg);
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    logger.logIfEnabled(fqcn, Level.TRACE, null, format, arg1, arg2);
  }

  @Override
  public void trace(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.TRACE, null, format, arguments);
  }

  @Override
  public void trace(String msg, Throwable t) {
    logger.logIfEnabled(fqcn, Level.TRACE, null, msg, t);
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  public void debug(String format) {
    logger.logIfEnabled(fqcn, Level.DEBUG, null, format);
  }

  @Override
  public void debug(String format, Object arg) {
    logger.logIfEnabled(fqcn, Level.DEBUG, null, format, arg);
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    logger.logIfEnabled(fqcn, Level.DEBUG, null, format, arg1, arg2);

  }

  @Override
  public void debug(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.DEBUG, null, format, arguments);
  }

  @Override
  public void debug(String msg, Throwable t) {
    logger.logIfEnabled(fqcn, Level.DEBUG, null, msg, t);
  }

  @Override
  public boolean isInfoEnabled() {
    return isInfoEnabled();
  }

  @Override
  public void info(String format) {
    logger.logIfEnabled(fqcn, Level.INFO, null, format);
  }

  @Override
  public void info(String format, Object arg) {
    logger.logIfEnabled(fqcn, Level.INFO, null, format, arg);
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    logger.logIfEnabled(fqcn, Level.INFO, null, format, arg1, arg2);
  }

  @Override
  public void info(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.INFO, null, format, arguments);
  }

  @Override
  public void info(String msg, Throwable t) {
    logger.logIfEnabled(fqcn, Level.INFO, null, msg, t);
  }

  @Override
  public boolean isWarnEnabled() {
    return isWarnEnabled();
  }

  @Override
  public void warn(String format) {
    logger.logIfEnabled(fqcn, Level.WARN, null, format);
  }

  @Override
  public void warn(String format, Object arg) {
    logger.logIfEnabled(fqcn, Level.WARN, null, format, arg);
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    logger.logIfEnabled(fqcn, Level.WARN, null, format, arg1, arg2);
  }

  @Override
  public void warn(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.WARN, null, format, arguments);
  }

  @Override
  public void warn(String msg, Throwable t) {
    logger.logIfEnabled(fqcn, Level.WARN, null, msg, t);
  }

  @Override
  public boolean isErrorEnabled() {
    return isErrorEnabled();
  }

  @Override
  public void error(String format) {
    logger.logIfEnabled(fqcn, Level.ERROR, null, format);
  }

  @Override
  public void error(String format, Object arg) {
    logger.logIfEnabled(fqcn, Level.ERROR, null, format, arg);
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    logger.logIfEnabled(fqcn, Level.ERROR, null, format, arg1, arg2);
  }

  @Override
  public void error(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.ERROR, null, format, arguments);
  }

  @Override
  public void error(String msg, Throwable t) {
    logger.logIfEnabled(fqcn, Level.ERROR, null, msg, t);
  }

}
