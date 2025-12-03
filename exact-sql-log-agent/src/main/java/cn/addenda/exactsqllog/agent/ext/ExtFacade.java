package cn.addenda.exactsqllog.agent.ext;

import cn.addenda.exactsqllog.agent.AgentPackage;
import cn.addenda.exactsqllog.agent.ExactSqlLogAgentStartException;
import cn.addenda.exactsqllog.agent.util.FileUtils;
import cn.addenda.exactsqllog.common.bo.PreparedSqlBo;
import cn.addenda.exactsqllog.common.bo.SqlBo;
import cn.addenda.exactsqllog.common.config.EslConnectionConfig;
import cn.addenda.exactsqllog.common.jvm.JVMShutdown;
import cn.addenda.exactsqllog.common.jvm.JVMShutdownCallback;
import cn.addenda.exactsqllog.facade.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExtFacade {

  static {
    initConfig();
    initHttpFacade();
    initJsonFacade();
    initLogFacade();
  }

  // ------------------------
  //   初始化HttpFacade及实现
  // ------------------------

  static String httpFacadeImpl;
  static HttpFacade httpFacade;
  static String receivePreparedSqlBoUrl;
  static String receiveSqlBoUrl;
  static String receiveEslConnectionConfigUrl;

  private static void initHttpFacade() {
    httpFacadeImpl = AgentPackage.getAgentProperties().getProperty("httpFacade.impl");
    Consumer<ClassLoader> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultHttpImplClass = Class.forName(httpFacadeImpl, true, extClassLoader);
        httpFacade = (HttpFacade) eslDefaultHttpImplClass
                .getConstructor()
                .newInstance();
        if (httpFacade instanceof JVMShutdownCallback) {
          JVMShutdown.getInstance().addJvmShutdownCallback((JVMShutdownCallback) httpFacade);
        }
      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new ExactSqlLogAgentStartException(
                String.format("Cannot init httpFacade: [%s].", httpFacadeImpl), e);
      }
    };
    acceptWithExtClassLoader(runnable);
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

  // ------------------------
  //   初始化JsonFacade及实现
  // ------------------------

  static String jsonFacadeImpl;

  static JsonFacade jsonFacade;

  private static void initJsonFacade() {
    jsonFacadeImpl = AgentPackage.getAgentProperties().getProperty("jsonFacade.impl");

    Consumer<ClassLoader> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultJsonImplClass = Class.forName(jsonFacadeImpl, true, extClassLoader);
        jsonFacade = (JsonFacade) eslDefaultJsonImplClass
                .getConstructor()
                .newInstance();
        if (jsonFacade instanceof JVMShutdownCallback) {
          JVMShutdown.getInstance().addJvmShutdownCallback((JVMShutdownCallback) jsonFacade);
        }
      } catch (ClassNotFoundException | InvocationTargetException |
               InstantiationException | IllegalAccessException | NoSuchMethodException e) {
        throw new ExactSqlLogAgentStartException(
                String.format("Cannot init jsonFacade: [%s].", jsonFacadeImpl), e);
      }
    };
    acceptWithExtClassLoader(runnable);
    receivePreparedSqlBoUrl = AgentPackage.getAgentProperties().getProperty("receivePreparedSqlBo.url");
    receiveSqlBoUrl = AgentPackage.getAgentProperties().getProperty("receiveSqlBo.url");
    receiveEslConnectionConfigUrl = AgentPackage.getAgentProperties().getProperty("receiveEslConnectionConfig.url");
  }

  public static String toStr(Object input) {
    return jsonFacade.toStr(input);
  }

  public static <T> T toObj(String inputJson, Class<T> clazz) {
    return jsonFacade.toObj(inputJson, clazz);
  }

  // ------------------
  //   初始化LogFacade
  // ------------------

  static String logFacadeImpl;

  private static void initLogFacade() {
    logFacadeImpl = AgentPackage.getAgentProperties().getProperty("logFacade.impl");
  }

  private static LogFacade _createLogFacade(String name) {
    Function<ClassLoader, LogFacade> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultLogImplClass = Class.forName(logFacadeImpl, true, extClassLoader);
        return (LogFacade) eslDefaultLogImplClass
                .getConstructor(String.class)
                .newInstance(name);

      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new ExactSqlLogAgentStartException(
                String.format("Cannot create logFacade: [%s].", logFacadeImpl), e);
      }
    };
    return applyWithExtClassLoader(runnable);
  }

  private static LogFacade _createLogFacade(String name, String fqcn) {
    Function<ClassLoader, LogFacade> runnable = extClassLoader -> {
      try {
        Class<?> eslDefaultLogImplClass = Class.forName(logFacadeImpl, true, extClassLoader);
        return (LogFacade) eslDefaultLogImplClass
                .getConstructor(String.class, String.class)
                .newInstance(name, fqcn);

      } catch (InstantiationException | IllegalAccessException |
               InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
        throw new ExactSqlLogAgentStartException(
                String.format("Cannot create logFacade: [%s].", logFacadeImpl), e);
      }
    };
    return applyWithExtClassLoader(runnable);
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

  // --------------------------------------------------------------------
  // 在agent启动的时候，加载配置文件并将配置文件分发到各个ext的PropertiesAware里面
  // --------------------------------------------------------------------

  static File httpConfigFile;
  static Properties httpConfigProperties;
  static File jsonConfigFile;
  static Properties jsonConfigProperties;
  static File logConfigFile;
  static Properties logConfigProperties;

  private static void initConfig() {
    httpConfigFile = new File(ExtClassLoader.getExtHttpPath(), "http.properties");
    httpConfigProperties = FileUtils.loadProperties(httpConfigFile);
    jsonConfigFile = new File(ExtClassLoader.getExtJsonPath(), "json.properties");
    jsonConfigProperties = FileUtils.loadProperties(jsonConfigFile);
    logConfigFile = new File(ExtClassLoader.getExtLogPath(), "log.properties");
    logConfigProperties = FileUtils.loadProperties(logConfigFile);
    dispatchConfig();
  }

  private static void dispatchConfig() {
    Consumer<ClassLoader> consumer = classLoader -> {
      List<String> awareNameList = getAwareNameList(classLoader);
      for (String awareName : awareNameList) {
        loadClassAndInvokeAware(classLoader, awareName);
      }
    };

    acceptWithExtClassLoader(consumer);
  }

  private static void loadClassAndInvokeAware(ClassLoader classLoader, String awareName) {
    Class<?> awareClass;
    try {
      awareClass = classLoader.loadClass(awareName);
    } catch (ClassNotFoundException e) {
      throw new ExactSqlLogAgentStartException(
              String.format("Error loading awareClass, awareName: %s.", awareName), e);
    }
    Method[] declaredMethods = awareClass.getDeclaredMethods();
    for (Method declaredMethod : declaredMethods) {
      if (!Modifier.isStatic(declaredMethod.getModifiers())) {
        continue;
      }
      int parameterCount = declaredMethod.getParameterCount();
      if (parameterCount != 1) {
        continue;
      }
      Class<?> parameterType = declaredMethod.getParameterTypes()[0];
      if (!parameterType.isAssignableFrom(Properties.class)) {
        continue;
      }

      AgentPropertiesAware agentPropertiesAware = declaredMethod.getAnnotation(AgentPropertiesAware.class);
      if (agentPropertiesAware != null) {
        invokeAwareMethod(declaredMethod, AgentPackage.getAgentProperties(), awareClass);
      }
      ExtPropertiesAware extPropertiesAware = declaredMethod.getAnnotation(ExtPropertiesAware.class);
      if (extPropertiesAware != null) {
        if (agentPropertiesAware != null) {
          throw new ExactSqlLogAgentStartException(
                  String.format("@AgentPropertiesAware and @ExtPropertiesAware can not annotate the same method: %s.", declaredMethod));
        }

        FacadeType facadeType = extPropertiesAware.value();
        Properties customProperties;
        switch (facadeType) {
          case LOG:
            customProperties = logConfigProperties;
            break;
          case HTTP:
            customProperties = httpConfigProperties;
            break;
          case JSON:
            customProperties = jsonConfigProperties;
            break;
          default:
            throw new ExactSqlLogAgentStartException(
                    String.format("Unsupported facade type: %s, awareClass: %s, awareMethod: %s.", facadeType, awareClass, declaredMethod));
        }
        invokeAwareMethod(declaredMethod, customProperties, awareClass);
      }
    }
  }

  private static void invokeAwareMethod(Method method, Properties properties, Class<?> awareClass) {
    try {
      method.invoke(null, properties);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new ExactSqlLogAgentStartException(
              String.format("invoke aware method error, awareClass: %s, awareMethod: %s.", awareClass, method), e);
    }
  }

  private static final String AWARE_CONF_RESOURCE = "META-INF/esl.ext.conf.aware";

  private static List<String> getAwareNameList(ClassLoader classLoader) {
    List<String> nameList = new ArrayList<>();
    Enumeration<URL> resources;
    try {
      resources = classLoader.getResources(AWARE_CONF_RESOURCE);
    } catch (IOException e) {
      throw new ExactSqlLogAgentStartException(
              String.format("Error getting resources from '%s'.", AWARE_CONF_RESOURCE), e);
    }
    while (resources.hasMoreElements()) {
      URL url = resources.nextElement();
      InputStream inputStream = null;
      BufferedReader bufferedReader = null;
      try {
        inputStream = url.openStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String s = bufferedReader.readLine();
        if (s != null && !s.trim().isEmpty()) {
          nameList.add(s);
        }
      } catch (IOException x) {
        throw new ExactSqlLogAgentStartException(
                String.format("Error reading configuration file, url: %s.", url.getPath()), x);
      } finally {
        try {
          if (bufferedReader != null) bufferedReader.close();
          if (inputStream != null) inputStream.close();
        } catch (IOException y) {
          throw new ExactSqlLogAgentStartException(
                  String.format("Error closing configuration file, url: %s.", url.getPath()), y);
        }
      }
    }
    return nameList;
  }

  // ---------------------
  // classLoader的函数表达式
  // ---------------------

  private static void acceptWithExtClassLoader(Consumer<ClassLoader> consumer) {
    ExtClassLoader extClassLoader = ExtClassLoader.getInstance();
//    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//    Thread.currentThread().setContextClassLoader(extClassLoader);
//    try {
    consumer.accept(extClassLoader);
//    } finally {
//      Thread.currentThread().setContextClassLoader(contextClassLoader);
//    }
  }

  private static <T> T applyWithExtClassLoader(Function<ClassLoader, T> function) {
    ExtClassLoader extClassLoader = ExtClassLoader.getInstance();
//    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//    Thread.currentThread().setContextClassLoader(extClassLoader);
//    try {
    return function.apply(extClassLoader);
//    } finally {
//      Thread.currentThread().setContextClassLoader(contextClassLoader);
//    }
  }

}
