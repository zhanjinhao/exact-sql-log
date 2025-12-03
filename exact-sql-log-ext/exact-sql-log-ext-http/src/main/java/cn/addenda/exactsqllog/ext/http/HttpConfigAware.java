package cn.addenda.exactsqllog.ext.http;

import cn.addenda.exactsqllog.facade.AgentPropertiesAware;
import cn.addenda.exactsqllog.facade.ExtPropertiesAware;
import cn.addenda.exactsqllog.facade.FacadeType;

import java.util.Properties;

public class HttpConfigAware {

  private static Properties agentProperties;
  private static Properties httpProperties;

  public static Properties getAgentProperties() {
    return agentProperties;
  }

  @AgentPropertiesAware
  public static void setAgentProperties(Properties agentProperties) {
    HttpConfigAware.agentProperties = agentProperties;
  }

  public static Properties getHttpProperties() {
    return httpProperties;
  }

  @ExtPropertiesAware(value = FacadeType.HTTP)
  public static void setHttpProperties(Properties httpProperties) {
    HttpConfigAware.httpProperties = httpProperties;
  }

}
