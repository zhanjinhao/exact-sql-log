package cn.addenda.exactsqllog.proxy.system;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyDefaultSystemLoggerFactory implements SystemLoggerFactory {

  @Getter
  private static final ProxyDefaultSystemLoggerFactory instance;

  static {
    instance = new ProxyDefaultSystemLoggerFactory();
  }

  private final Map<String, SystemLogger> systemLoggerMap = new ConcurrentHashMap<>();

  private ProxyDefaultSystemLoggerFactory() {
  }

  @Override
  public SystemLogger getSystemLogger(String name) {
    return systemLoggerMap.computeIfAbsent(name, ProxyDefaultSystemLogger::new);
  }

  @Override
  public SystemLogger getSystemLogger(Class<?> clazz) {
    return getSystemLogger(clazz.getName());
  }

}
