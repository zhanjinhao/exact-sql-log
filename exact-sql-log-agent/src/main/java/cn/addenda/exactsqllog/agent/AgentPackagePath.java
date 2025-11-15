package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.agent.util.FileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * AgentPackagePath is a flag and finder to locate the SkyWalking agent.jar. It gets the absolute path of the agent jar.
 * The path is the required metadata for agent core looking up the plugins and toolkit activations. If the lookup
 * mechanism fails, the agent will exit directly.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentPackagePath {

  private static File AGENT_PACKAGE_PATH;
  private static Properties AGENT_PROPERTIES;

  public static synchronized File getPath() {
    if (AGENT_PACKAGE_PATH == null) {
      AGENT_PACKAGE_PATH = findPath();
    }
    return AGENT_PACKAGE_PATH;
  }

  public static synchronized String getPathString() {
    File path = getPath();
    return path.getAbsolutePath();
  }

  public static synchronized boolean isPathFound() {
    return AGENT_PACKAGE_PATH != null;
  }

  private static synchronized File findPath() {
    String classResourcePath = AgentPackagePath.class.getName().replaceAll("\\.", "/") + ".class";

    URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
    if (resource != null) {
      String urlString = resource.toString();


      int insidePathIndex = urlString.indexOf('!');
      boolean isInJar = insidePathIndex > -1;

      if (isInJar) {
        urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
        File agentJarFile = null;
        try {
          agentJarFile = new File(new URL(urlString).toURI());
        } catch (MalformedURLException | URISyntaxException e) {
          throw new ExactSqlLogAgentBootstrapException(String.format("Cannot find agent path. ClassResourcePath is [%s].", classResourcePath));
        }
        if (agentJarFile.exists()) {
          return agentJarFile.getParentFile();
        }
      } else {
        int prefixLength = "file:".length();
        String classLocation = urlString.substring(
                prefixLength, urlString.length() - classResourcePath.length());
        return new File(classLocation);
      }
    }

    throw new ExactSqlLogAgentBootstrapException(String.format("Cannot find agent path. ClassResourcePath is [%s].", classResourcePath));
  }

  private static synchronized File getAgentConf() {
    File path = getPath();
    return new File(path, "agent.properties");
  }

  public static synchronized Properties getAgentProperties() {
    if (AGENT_PROPERTIES != null) {
      return new Properties(AGENT_PROPERTIES);
    }
    AGENT_PROPERTIES = FileUtils.loadProperties(AgentPackagePath.getAgentConf());
    return new Properties(AGENT_PROPERTIES);
  }

}
