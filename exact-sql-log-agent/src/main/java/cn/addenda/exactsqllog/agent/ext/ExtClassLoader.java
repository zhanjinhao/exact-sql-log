package cn.addenda.exactsqllog.agent.ext;

import cn.addenda.exactsqllog.agent.AgentPackage;
import cn.addenda.exactsqllog.agent.ExactSqlLogAgentBootstrapException;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class ExtClassLoader extends URLClassLoader {

  @Getter
  private final List<String> prefixList = new ArrayList<>();

  static {
    ClassLoader.registerAsParallelCapable();
  }

  private static ExtClassLoader instance;

  @Getter
  private static final File extPath;

  @Getter
  private static final File extLogPath;

  @Getter
  private static final File extHttpPath;

  @Getter
  private static final File extJsonPath;

  static {
    extPath = new File(AgentPackage.getPath(), "ext");
    extLogPath = new File(extPath, "log");
    extHttpPath = new File(extPath, "http");
    extJsonPath = new File(extPath, "json");
  }

  public ExtClassLoader(ClassLoader parent) {
    super(new URL[0], parent);
    this.addUrls();
    this.addPrefixes();
  }

  private void addPrefixes() {
    List<String> extPrefixList = getExtPrefixList();
    this.prefixList.addAll(extPrefixList);
  }

  private void addUrls() {
    List<File> extLibPathList = Arrays.asList(extLogPath, extHttpPath, extJsonPath);
    List<URL> urlList = new ArrayList<>();
    for (File extLibPath : extLibPathList) {
      urlList.addAll(Arrays.stream(ExtClassLoader.findJarUrls(extLibPath)).collect(Collectors.toList()));
    }
    // 添加扩展jar文件
    for (URL url : urlList) {
      addURL(url);
    }
  }

  protected static URL[] findJarUrls(File extLibDir) {
    List<URL> urls = new ArrayList<>();
    if (extLibDir.exists() && extLibDir.isDirectory()) {
      File[] jars = extLibDir.listFiles((dir, name) -> name.endsWith(".jar"));

      if (jars != null) {
        for (File jar : jars) {
          try {
            urls.add(jar.toURI().toURL());
          } catch (MalformedURLException e) {
            throw new ExactSqlLogAgentBootstrapException(String.format("Cannot get url of jar: [%s].", jar.getAbsolutePath()), e);
          }
        }
      }
    }
    return urls.toArray(new URL[0]);
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {
      Class<?> loadedClass = null;
      loadedClass = findLoadedClass(name);
      if (loadedClass != null) {
        return loadedClass;
      }

      boolean shouldIsolate = false;
      for (String prefix : prefixList) {
        if (name.startsWith(prefix)) {
          shouldIsolate = true;
          break;
        }
      }

      if (shouldIsolate) {
        loadedClass = findClass(name);
        if (resolve) {
          resolveClass(loadedClass);
        }
        return loadedClass;
      }

      return super.loadClass(name, resolve);
    }
  }

  private static List<String> getExtPrefixList() {
    List<String> httpPrefixList = new ArrayList<>();
    httpPrefixList.add("org.apache.hc.");
    httpPrefixList.add("cn.addenda.exactsqllog.ext.http.");

    httpPrefixList.add("com.fasterxml.jackson.");
    httpPrefixList.add("cn.addenda.exactsqllog.ext.json.");

    httpPrefixList.add("org.slf4j.");
    httpPrefixList.add("org.apache.logging.log4j.");
    httpPrefixList.add("org.apache.logging.slf4j.");
    httpPrefixList.add("cn.addenda.exactsqllog.ext.log.");

    return httpPrefixList;
  }

  public static synchronized ExtClassLoader getInstance() {
    initInstance();
    return instance;
  }

  public static synchronized void initInstance() {
    if (instance == null) {
      ClassLoader parentClassLoader = ClassLoader.getSystemClassLoader();
      instance = new ExtClassLoader(parentClassLoader);
    }
  }

  @Override
  public Enumeration<URL> getResources(String name) throws IOException {
    if ("org/slf4j/impl/StaticLoggerBinder.class".equals(name)) {
      return findResources(name);
    }
    return super.getResources(name);
  }

}
