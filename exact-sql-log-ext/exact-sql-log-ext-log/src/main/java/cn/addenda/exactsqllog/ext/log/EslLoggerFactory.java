package cn.addenda.exactsqllog.ext.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.spi.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class EslLoggerFactory {

  private Properties agentProperties;
  private File confFile;

  public EslLoggerFactory(Properties agentProperties, File confFile) {
    this.agentProperties = agentProperties;
    this.confFile = confFile;
    LoggerContext context = LogManager.getContext(null, false, confFile.toURI());
  }

  public Logger getLogger(Class<?> clazz) {
    return LoggerFactory.getLogger(clazz);
  }

  public Logger getLogger(String clazz) {
    return LoggerFactory.getLogger(clazz);
  }

}
