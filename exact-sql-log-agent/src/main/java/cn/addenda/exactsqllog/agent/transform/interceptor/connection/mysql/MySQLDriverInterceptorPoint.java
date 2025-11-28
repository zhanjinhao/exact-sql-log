package cn.addenda.exactsqllog.agent.transform.interceptor.connection.mysql;

import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
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
