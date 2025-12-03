package cn.addenda.exactsqllog.ext.json;

import cn.addenda.exactsqllog.facade.JsonFacade;

public class EslDefaultJsonImpl implements JsonFacade {

  public EslDefaultJsonImpl() {
  }

  @Override
  public String toStr(Object input) {
    return ExactSqlLogExtJsonException.getWithExactSqlLogExtException(
            () -> EslJsonUtils.toStr(input)
    );
  }

  @Override
  public <T> T toObj(String inputJson, Class<T> clazz) {
    return ExactSqlLogExtJsonException.getWithExactSqlLogExtException(
            () -> EslJsonUtils.toObj(inputJson, clazz)
    );
  }

}
