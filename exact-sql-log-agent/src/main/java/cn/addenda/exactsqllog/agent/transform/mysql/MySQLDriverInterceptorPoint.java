package cn.addenda.exactsqllog.agent.transform.mysql;

import cn.addenda.exactsqllog.agent.transform.Interceptor;
import cn.addenda.exactsqllog.agent.transform.InterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class MySQLDriverInterceptorPoint implements InterceptorPoint {

  @Override
  public ElementMatcher<MethodDescription> getMethodsMatcher() {
    return ElementMatchers.named("connect");
  }

  @Override
  public Interceptor getInterceptor() {
    return new MySQLDriverConnectInterceptor();
  }

}
