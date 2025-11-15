package cn.addenda.exactsqllog.agent.util;

import cn.addenda.exactsqllog.agent.ExactSqlLogAgentBootstrapException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

  public static Properties loadProperties(File file) {
    Properties p = new Properties();
    try (InputStream inputStream = file.toURI().toURL().openStream()) {
      p.load(inputStream);
    } catch (Exception e) {
      throw new ExactSqlLogAgentBootstrapException(String.format("loadProperties error: [%s].", file.getAbsolutePath()), e);
    }
    return p;
  }

}
