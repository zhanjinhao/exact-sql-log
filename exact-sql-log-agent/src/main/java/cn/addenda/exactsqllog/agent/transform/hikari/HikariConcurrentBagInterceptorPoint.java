package cn.addenda.exactsqllog.agent.transform.hikari;

import cn.addenda.exactsqllog.agent.transform.Interceptor;
import cn.addenda.exactsqllog.agent.transform.InterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class HikariConcurrentBagInterceptorPoint implements InterceptorPoint {

  @Override
  public ElementMatcher<MethodDescription> getMethodsMatcher() {
    return ElementMatchers.named("borrow");
  }

  @Override
  public Interceptor getInterceptor() {
    return new HikariConcurrentBagBorrowInterceptor();
  }

}
