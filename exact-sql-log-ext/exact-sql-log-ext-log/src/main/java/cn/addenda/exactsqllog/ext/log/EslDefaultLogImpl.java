package cn.addenda.exactsqllog.ext.log;

import cn.addenda.exactsqllog.facade.LogFacade;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.util.Properties;

public class EslDefaultLogImpl implements LogFacade {

  private final Properties agentProperties;
  private final File confFile;
  private final String name;
  private final String fqcn;
  private final Logger logger;

  public EslDefaultLogImpl(Properties agentProperties, File confFile, String name, String fqcn) {
    this.agentProperties = agentProperties;
    this.confFile = confFile;
    this.name = name;
    this.fqcn = fqcn;
    this.logger = LoggerContext.getContext(null, false, confFile.toURI()).getLogger(name);
  }

  public EslDefaultLogImpl(Properties agentProperties, File confFile, String name) {
    this(agentProperties, confFile, name, EslDefaultLogImpl.class.getName());
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
