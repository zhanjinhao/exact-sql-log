package cn.addenda.exactsqllog.ext.json;

import cn.addenda.exactsqllog.facade.AgentPropertiesAware;
import cn.addenda.exactsqllog.facade.ExtPropertiesAware;
import cn.addenda.exactsqllog.facade.FacadeType;

import java.util.Properties;

public class JsonConfigAware {

  private static Properties agentProperties;
  private static Properties jsonProperties;

  public static Properties getAgentProperties() {
    return agentProperties;
  }

  @AgentPropertiesAware
  public static void setAgentProperties(Properties agentProperties) {
    JsonConfigAware.agentProperties = agentProperties;
  }

  public static Properties getJsonProperties() {
    return jsonProperties;
  }

  @ExtPropertiesAware(value = FacadeType.JSON)
  public static void setJsonProperties(Properties jsonProperties) {
    JsonConfigAware.jsonProperties = jsonProperties;
  }

}
