package cn.addenda.exactsqllog.agent.transform.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface InterceptorPoint {

  ElementMatcher<MethodDescription> getMethodsMatcher();

  Interceptor getInterceptor();

}
