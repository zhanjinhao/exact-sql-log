package cn.addenda.exactsqllog.ext.facade;

public interface JsonFacade {

  String toStr(Object input);

  <T> T toObj(String inputJson, Class<T> clazz);

}
