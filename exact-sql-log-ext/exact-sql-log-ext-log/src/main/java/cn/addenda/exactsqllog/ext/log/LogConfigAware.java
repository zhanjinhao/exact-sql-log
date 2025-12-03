package cn.addenda.exactsqllog.ext.log;

import cn.addenda.exactsqllog.facade.AgentPropertiesAware;
import cn.addenda.exactsqllog.facade.ExtPropertiesAware;
import cn.addenda.exactsqllog.facade.FacadeType;

import java.util.Properties;

public class LogConfigAware {

  private static Properties agentProperties;
  private static Properties logProperties;

  public static Properties getAgentProperties() {
    return agentProperties;
  }

  @AgentPropertiesAware
  public static void setAgentProperties(Properties agentProperties) {
    LogConfigAware.agentProperties = agentProperties;
  }

  public static Properties getLogProperties() {
    return logProperties;
  }

  @ExtPropertiesAware(value = FacadeType.LOG)
  public static void setLogProperties(Properties httpProperties) {
    LogConfigAware.logProperties = httpProperties;
  }

}
