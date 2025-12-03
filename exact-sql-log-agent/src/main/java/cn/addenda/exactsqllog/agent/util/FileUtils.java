package cn.addenda.exactsqllog.agent.util;

import cn.addenda.exactsqllog.agent.AgentPackage;
import cn.addenda.exactsqllog.agent.ExactSqlLogAgentBootstrapException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

  private static PropertyPlaceholderHelper propertyPlaceholderHelper;
  private static Properties propertiesForReplacePlaceholders;

  public static Properties loadProperties(File file) {
    Properties p = new Properties();
    try (InputStream inputStream = file.toURI().toURL().openStream()) {
      p.load(inputStream);
    } catch (Exception e) {
      throw new ExactSqlLogAgentBootstrapException(String.format("loadProperties error: [%s].", file.getAbsolutePath()), e);
    }

    Properties p2 = new Properties();

    p.forEach((key, value)
            -> p2.put(key, getPropertyPlaceholderHelper().replacePlaceholders((String) value, getPropertiesForReplacePlaceholders())));

    return p2;
  }

  private static synchronized Properties getPropertiesForReplacePlaceholders() {
    if (propertiesForReplacePlaceholders == null) {
      propertiesForReplacePlaceholders = new Properties();
      propertiesForReplacePlaceholders.put("agentPackagePath", AgentPackage.getPathString());
    }
    return propertiesForReplacePlaceholders;
  }

  private static synchronized PropertyPlaceholderHelper getPropertyPlaceholderHelper() {
    if (propertyPlaceholderHelper == null) {
      propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}", null, false);
    }
    return propertyPlaceholderHelper;
  }

}
