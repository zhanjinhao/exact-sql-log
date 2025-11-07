package cn.addenda.exactsqllog.proxy.system;

public interface SystemLoggerFactory {

  SystemLogger getSystemLogger(String name);

  SystemLogger getSystemLogger(Class<?> clazz);

}
