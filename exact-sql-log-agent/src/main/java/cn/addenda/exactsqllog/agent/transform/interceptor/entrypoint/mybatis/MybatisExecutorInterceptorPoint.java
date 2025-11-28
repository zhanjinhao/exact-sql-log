package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.mybatis;

import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class MybatisExecutorInterceptorPoint implements InterceptorPoint {

  @Override
  public ElementMatcher<MethodDescription> getMethodsMatcher() {
    return ElementMatchers.named("update")
            .or(ElementMatchers.named("query"))
            .or(ElementMatchers.named("queryCursor"));
  }

  @Override
  public Interceptor getInterceptor() {
    return new MybatisExecutorInterceptor();
  }

}
