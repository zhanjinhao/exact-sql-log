package cn.addenda.exactsqllog.ext.json;

import cn.addenda.exactsqllog.ext.ExactSqlLogExtException;
import cn.addenda.exactsqllog.ext.facade.JsonFacade;

import java.util.Properties;

public class EslDefaultJsonImpl implements JsonFacade {

  private final Properties agentProperties;
  private final Properties httpProperties;

  public EslDefaultJsonImpl(Properties agentProperties, Properties httpProperties) {
    this.agentProperties = agentProperties;
    this.httpProperties = httpProperties;
  }

  @Override
  public String toStr(Object input) {
    return ExactSqlLogExtException.getWithExactSqlLogExtException(
            () -> EslJsonUtils.toStr(input)
    );
  }

  @Override
  public <T> T toObj(String inputJson, Class<T> clazz) {
    return ExactSqlLogExtException.getWithExactSqlLogExtException(
            () -> EslJsonUtils.toObj(inputJson, clazz)
    );
  }

}
