package cn.addenda.exactsqllog.agent.transform;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface InterceptorPoint {

  ElementMatcher<MethodDescription> getMethodsMatcher();

  Interceptor getInterceptor();

}
