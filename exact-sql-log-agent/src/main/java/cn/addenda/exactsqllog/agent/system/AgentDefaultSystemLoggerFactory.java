package cn.addenda.exactsqllog.agent.system;

import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.system.SystemLoggerFactory;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AgentDefaultSystemLoggerFactory implements SystemLoggerFactory {

  @Getter
  private static final AgentDefaultSystemLoggerFactory instance;

  static {
    instance = new AgentDefaultSystemLoggerFactory();
  }

  private final Map<String, SystemLogger> systemLoggerMap = new ConcurrentHashMap<>();

  private AgentDefaultSystemLoggerFactory() {
  }

  @Override
  public SystemLogger getSystemLogger(String name) {
    return systemLoggerMap.computeIfAbsent(name, AgentDefaultSystemLogger::new);
  }

  @Override
  public SystemLogger getSystemLogger(Class<?> clazz) {
    return getSystemLogger(clazz.getName());
  }

}
