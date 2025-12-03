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
  public void trace(String format) {
    logger.logIfEnabled(fqcn, Level.TRACE, null, format);
  }

  @Override
  public void trace(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.TRACE, null, format, arguments);
  }

  @Override
  public void debug(String format) {
    logger.logIfEnabled(fqcn, Level.DEBUG, null, format);
  }

  @Override
  public void debug(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.DEBUG, null, format, arguments);
  }

  @Override
  public void info(String format) {
    logger.logIfEnabled(fqcn, Level.INFO, null, format);
  }

  @Override
  public void info(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.INFO, null, format, arguments);
  }

  @Override
  public void warn(String format) {
    logger.logIfEnabled(fqcn, Level.WARN, null, format);
  }

  @Override
  public void warn(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.WARN, null, format, arguments);
  }

  @Override
  public void error(String format) {
    logger.logIfEnabled(fqcn, Level.ERROR, null, format);
  }

  @Override
  public void error(String format, Object... arguments) {
    logger.logIfEnabled(fqcn, Level.ERROR, null, format, arguments);
  }

}
