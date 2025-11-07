package cn.addenda.exactsqllog.agent.system;

import cn.addenda.exactsqllog.agent.ext.ExtFacade;
import cn.addenda.exactsqllog.ext.facade.LogFacade;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;

public class AgentDefaultSystemLogger implements SystemLogger {

  private final String name;

  private final LogFacade logFacade;

  public AgentDefaultSystemLogger(String name) {
    this.name = name;
    this.logFacade = ExtFacade.createLogFacade(name, AgentDefaultSystemLogger.class.getName());
  }

  @Override
  public void trace(String msg) {
    logFacade.trace(msg);
  }

  @Override
  public void trace(String format, Object... arguments) {
    logFacade.trace(format, arguments);
  }

  @Override
  public void debug(String msg) {
    logFacade.debug(msg);
  }

  @Override
  public void debug(String format, Object... arguments) {
    logFacade.debug(format, arguments);
  }

  @Override
  public void info(String msg) {
    logFacade.info(msg);
  }

  @Override
  public void info(String format, Object... arguments) {
    logFacade.info(format, arguments);
  }

  @Override
  public void warn(String msg) {
    logFacade.warn(msg);
  }

  @Override
  public void warn(String format, Object... arguments) {
    logFacade.warn(format, arguments);
  }

  @Override
  public void error(String msg) {
    logFacade.error(msg);
  }

  @Override
  public void error(String format, Object... arguments) {
    logFacade.error(format, arguments);
  }

}
