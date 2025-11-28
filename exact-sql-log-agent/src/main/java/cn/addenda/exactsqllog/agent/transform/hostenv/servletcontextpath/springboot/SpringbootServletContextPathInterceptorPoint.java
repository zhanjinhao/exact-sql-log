package cn.addenda.exactsqllog.agent.transform.hostenv.servletcontextpath.springboot;

import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class SpringbootServletContextPathInterceptorPoint implements InterceptorPoint {

  @Override
  public ElementMatcher<MethodDescription> getMethodsMatcher() {
    return ElementMatchers.named("setContextPath");
  }

  @Override
  public Interceptor getInterceptor() {
    return new SpringbootServletContextPathSetContextPathInterceptor();
  }

}
