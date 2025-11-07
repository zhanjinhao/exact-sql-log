package cn.addenda.exactsqllog.agent.ext;

import cn.addenda.exactsqllog.agent.AgentPackagePath;
import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.common.util.FileUtils;
import cn.addenda.exactsqllog.ext.facade.HttpFacade;
import cn.addenda.exactsqllog.ext.facade.JsonFacade;
import cn.addenda.exactsqllog.ext.facade.LogFacade;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExtFacade {

  static {
    initHttpFacade();
    initJsonFacade();
    initLogFacade();
  }

  // ------------------------
  //   初始化HttpFacade及实现
  // ------------------------

  static File httpConfigFile;
  static Properties httpConfigProperties;
  static HttpFacade httpFacade;
  static String receiveExecutionUrl;

  private static void initHttpFacade() {
    httpConfigFile = new File(ExtClassLoader.getExtHttpPath(), "http.properties");
    httpConfigProperties = FileUtils.loadProperties(httpConfigFile);

    Consumer<ClassLoader> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultHttpImplClass = Class.forName("cn.addenda.exactsqllog.ext.http.EslDefaultHttpImpl", true, extClassLoader);
        httpFacade = (HttpFacade) eslDefaultHttpImplClass.getConstructor(Properties.class, Properties.class)
                .newInstance(AgentPackagePath.getAgentProperties(), httpConfigProperties);
      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    };
    runWithExtClassLoader(runnable);
  }

  // ------------------------
  //   初始化JsonFacade及实现
  // ------------------------

  static File jsonConfigFile;
  static Properties jsonConfigProperties;
  static JsonFacade jsonFacade;

  private static void initJsonFacade() {
    jsonConfigFile = new File(ExtClassLoader.getExtJsonPath(), "json.properties");

    Consumer<ClassLoader> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultJsonImplClass = Class.forName("cn.addenda.exactsqllog.ext.json.EslDefaultJsonImpl", true, extClassLoader);
        jsonFacade = (JsonFacade) eslDefaultJsonImplClass.getConstructor(Properties.class, Properties.class)
                .newInstance(AgentPackagePath.getAgentProperties(), jsonConfigProperties);

      } catch (ClassNotFoundException | InvocationTargetException |
               InstantiationException | IllegalAccessException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    };
    runWithExtClassLoader(runnable);
    receiveExecutionUrl = AgentPackagePath.getAgentProperties().getProperty("receiveExecution.url");
  }

  static File logConfigFile;

  private static void initLogFacade() {
    logConfigFile = new File(ExtClassLoader.getExtLogPath(), "log4j2.xml");
  }

  private static LogFacade _createLogFacade(String name) {
    Function<ClassLoader, LogFacade> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultLogImplClass = Class.forName("cn.addenda.exactsqllog.ext.log.EslDefaultLogImpl", true, extClassLoader);
        return (LogFacade) eslDefaultLogImplClass.getConstructor(Properties.class, File.class, String.class)
                .newInstance(AgentPackagePath.getAgentProperties(), logConfigFile, name);

      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    };
    return applyWithExtClassLoader(runnable);
  }

  private static LogFacade _createLogFacade(String name, String fqcn) {
    Function<ClassLoader, LogFacade> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultLogImplClass = Class.forName("cn.addenda.exactsqllog.ext.log.EslDefaultLogImpl", true, extClassLoader);
        return (LogFacade) eslDefaultLogImplClass.getConstructor(Properties.class, File.class, String.class, String.class)
                .newInstance(AgentPackagePath.getAgentProperties(), logConfigFile, name, fqcn);

      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    };
    return applyWithExtClassLoader(runnable);
  }

  private static void runWithExtClassLoader(Consumer<ClassLoader> consumer) {
    ExtClassLoader extClassLoader = ExtClassLoader.getInstance();
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(extClassLoader);
    try {
      consumer.accept(extClassLoader);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    }
  }

  private static <T> T applyWithExtClassLoader(Function<ClassLoader, T> function) {
    ExtClassLoader extClassLoader = ExtClassLoader.getInstance();
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(extClassLoader);
    try {
      return function.apply(extClassLoader);
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    }
  }

  public static void sendExecution(Execution execution) {
    httpFacade.sendRequest(receiveExecutionUrl, toStr(execution));
  }

  public static String toStr(Object input) {
    return jsonFacade.toStr(input);
  }

  public static <T> T toObj(String inputJson, Class<T> clazz) {
    return jsonFacade.toObj(inputJson, clazz);
  }

  private static final Map<String, LogFacade> logFacadeMap = new ConcurrentHashMap<>();

  public static LogFacade createLogFacade(String name) {
    return logFacadeMap.computeIfAbsent(name, ExtFacade::_createLogFacade);
  }

  public static LogFacade createLogFacade(Class<?> clazz) {
    return createLogFacade(clazz.getName());
  }

  public static LogFacade createLogFacade(String name, String fqcn) {
    return logFacadeMap.computeIfAbsent(name, s -> _createLogFacade(s, fqcn));
  }

  public static LogFacade createLogFacade(Class<?> clazz, String fqcn) {
    return createLogFacade(clazz.getName(), fqcn);
  }

}
