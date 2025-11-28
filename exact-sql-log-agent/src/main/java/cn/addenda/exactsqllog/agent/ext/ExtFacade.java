package cn.addenda.exactsqllog.agent.ext;

import cn.addenda.exactsqllog.agent.AgentPackagePath;
import cn.addenda.exactsqllog.agent.ExactSqlLogAgentBootstrapException;
import cn.addenda.exactsqllog.agent.util.FileUtils;
import cn.addenda.exactsqllog.common.bo.PreparedSqlBo;
import cn.addenda.exactsqllog.common.bo.SqlBo;
import cn.addenda.exactsqllog.common.config.EslConnectionConfig;
import cn.addenda.exactsqllog.common.jvm.JVMShutdown;
import cn.addenda.exactsqllog.common.jvm.JVMShutdownCallback;
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

  static String httpFacadeImpl;
  static File httpConfigFile;
  static Properties httpConfigProperties;
  static HttpFacade httpFacade;
  static String receivePreparedSqlBoUrl;
  static String receiveSqlBoUrl;
  static String receiveEslConnectionConfigUrl;

  private static void initHttpFacade() {
    httpFacadeImpl = AgentPackagePath.getAgentProperties().getProperty("httpFacade.impl");
    httpConfigFile = new File(ExtClassLoader.getExtHttpPath(), "http.properties");
    httpConfigProperties = FileUtils.loadProperties(httpConfigFile);

    Consumer<ClassLoader> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultHttpImplClass = Class.forName(httpFacadeImpl, true, extClassLoader);
        httpFacade = (HttpFacade) eslDefaultHttpImplClass.getConstructor(Properties.class, Properties.class)
                .newInstance(AgentPackagePath.getAgentProperties(), httpConfigProperties);
        if (httpFacade instanceof JVMShutdownCallback) {
          JVMShutdown.getInstance().addJvmShutdownCallback((JVMShutdownCallback) httpFacade);
        }
      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new ExactSqlLogAgentBootstrapException(String.format("Cannot init httpFacade: [%s].", httpFacadeImpl), e);
      }
    };
    runWithExtClassLoader(runnable);
  }

  // ------------------------
  //   初始化JsonFacade及实现
  // ------------------------

  static String jsonFacadeImpl;
  static File jsonConfigFile;
  static Properties jsonConfigProperties;
  static JsonFacade jsonFacade;

  private static void initJsonFacade() {
    jsonFacadeImpl = AgentPackagePath.getAgentProperties().getProperty("jsonFacade.impl");
    jsonConfigFile = new File(ExtClassLoader.getExtJsonPath(), "json.properties");
    jsonConfigProperties = FileUtils.loadProperties(jsonConfigFile);

    Consumer<ClassLoader> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultJsonImplClass = Class.forName(jsonFacadeImpl, true, extClassLoader);
        jsonFacade = (JsonFacade) eslDefaultJsonImplClass.getConstructor(Properties.class, Properties.class)
                .newInstance(AgentPackagePath.getAgentProperties(), jsonConfigProperties);
        if (jsonFacade instanceof JVMShutdownCallback) {
          JVMShutdown.getInstance().addJvmShutdownCallback((JVMShutdownCallback) jsonFacade);
        }
      } catch (ClassNotFoundException | InvocationTargetException |
               InstantiationException | IllegalAccessException | NoSuchMethodException e) {
        throw new ExactSqlLogAgentBootstrapException(String.format("Cannot init jsonFacade: [%s].", jsonFacadeImpl), e);
      }
    };
    runWithExtClassLoader(runnable);
    receivePreparedSqlBoUrl = AgentPackagePath.getAgentProperties().getProperty("receivePreparedSqlBo.url");
    receiveSqlBoUrl = AgentPackagePath.getAgentProperties().getProperty("receiveSqlBo.url");
    receiveEslConnectionConfigUrl = AgentPackagePath.getAgentProperties().getProperty("receiveEslConnectionConfig.url");
  }

  static String logFacadeImpl;
  static File logConfigFile;

  private static void initLogFacade() {
    logFacadeImpl = AgentPackagePath.getAgentProperties().getProperty("logFacade.impl");
    logConfigFile = new File(ExtClassLoader.getExtLogPath(), "log4j2.xml");
  }

  private static LogFacade _createLogFacade(String name) {
    Function<ClassLoader, LogFacade> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultLogImplClass = Class.forName(logFacadeImpl, true, extClassLoader);
        return (LogFacade) eslDefaultLogImplClass.getConstructor(Properties.class, File.class, String.class)
                .newInstance(AgentPackagePath.getAgentProperties(), logConfigFile, name);

      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new ExactSqlLogAgentBootstrapException(String.format("Cannot create logFacade: [%s].", logFacadeImpl), e);
      }
    };
    return applyWithExtClassLoader(runnable);
  }

  private static LogFacade _createLogFacade(String name, String fqcn) {
    Function<ClassLoader, LogFacade> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultLogImplClass = Class.forName(logFacadeImpl, true, extClassLoader);
        return (LogFacade) eslDefaultLogImplClass.getConstructor(Properties.class, File.class, String.class, String.class)
                .newInstance(AgentPackagePath.getAgentProperties(), logConfigFile, name, fqcn);

      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new ExactSqlLogAgentBootstrapException(String.format("Cannot create logFacade: [%s].", logFacadeImpl), e);
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

  public static void sendPreparedSqlBo(PreparedSqlBo preparedSqlBo) {
    httpFacade.sendRequest(receivePreparedSqlBoUrl, toStr(preparedSqlBo));
  }

  public static void sendSqlBo(SqlBo sqlBo) {
    httpFacade.sendRequest(receiveSqlBoUrl, toStr(sqlBo));
  }

  public static void sendEslConnectionConfig(EslConnectionConfig eslConnectionConfig) {
    httpFacade.sendRequest(receiveEslConnectionConfigUrl, toStr(eslConnectionConfig));
  }

  public static String toStr(Object input) {
    return jsonFacade.toStr(input);
  }

  public static <T> T toObj(String inputJson, Class<T> clazz) {
    return jsonFacade.toObj(inputJson, clazz);
  }

  private static final Map<String, LogFacade> logFacadeMap = new ConcurrentHashMap<>();

  public static LogFacade createLogFacade(String name) {
    return logFacadeMap.computeIfAbsent(name, s -> {
      LogFacade logFacade = _createLogFacade(s);
      if (logFacade instanceof JVMShutdownCallback) {
        JVMShutdown.getInstance().addJvmShutdownCallback((JVMShutdownCallback) logFacade);
      }
      return logFacade;
    });
  }

  public static LogFacade createLogFacade(Class<?> clazz) {
    return createLogFacade(clazz.getName());
  }

  public static LogFacade createLogFacade(String name, String fqcn) {
    return logFacadeMap.computeIfAbsent(name, s -> {
      LogFacade logFacade = _createLogFacade(s, fqcn);
      if (logFacade instanceof JVMShutdownCallback) {
        JVMShutdown.getInstance().addJvmShutdownCallback((JVMShutdownCallback) logFacade);
      }
      return logFacade;
    });
  }

  public static LogFacade createLogFacade(Class<?> clazz, String fqcn) {
    return createLogFacade(clazz.getName(), fqcn);
  }

}
