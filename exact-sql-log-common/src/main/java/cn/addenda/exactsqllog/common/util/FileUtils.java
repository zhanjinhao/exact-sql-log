package cn.addenda.exactsqllog.common.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class FileUtils {

  public static Properties loadProperties(File file) {
    Properties p = new Properties();
    try (InputStream inputStream = file.toURI().toURL().openStream();) {
      p.load(inputStream);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return p;
  }

}
