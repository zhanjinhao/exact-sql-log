package cn.addenda.exactsqllog.agent.transform.hostenv.servletcontextpath.springboot;

import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.match.NameMatch;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class SpringbootServletContextPathInterceptorPointDefine implements InterceptorPointDefine {

  public static final String SERVER_PROPERTIES_SERVLET_NAME = "org.springframework.boot.autoconfigure.web.ServerProperties$Servlet";

  @Override
  public NameMatch getEnhancedClass() {
    return NameMatch.of(SERVER_PROPERTIES_SERVLET_NAME);
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new SpringbootServletContextPathInterceptorPoint());
  }

}
