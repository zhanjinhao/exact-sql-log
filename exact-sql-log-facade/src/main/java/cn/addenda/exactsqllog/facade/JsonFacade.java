package cn.addenda.exactsqllog.facade;

public interface JsonFacade {

  String toStr(Object input);

  <T> T toObj(String inputJson, Class<T> clazz);

}
