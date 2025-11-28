package cn.addenda.exactsqllog.agent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 宿主环境类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HostEnv {

  private static String servletContextPath;

  public static String getServletContextPath() {
    return servletContextPath;
  }

  public static void setServletContextPath(String _servletContextPath) {
    servletContextPath = _servletContextPath;
    if (servletContextPath == null || servletContextPath.isEmpty()) {
      return;
    }
    if (!servletContextPath.startsWith("/")) {
      servletContextPath = "/" + servletContextPath;
    }
    if (servletContextPath.endsWith("/")) {
      servletContextPath = servletContextPath.substring(0, servletContextPath.length() - 1);
    }
  }

}
