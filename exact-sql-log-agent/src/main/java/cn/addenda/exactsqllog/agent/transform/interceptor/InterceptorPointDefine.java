package cn.addenda.exactsqllog.agent.transform.interceptor;

import cn.addenda.exactsqllog.agent.transform.OverrideCallable;
import cn.addenda.exactsqllog.agent.transform.match.ClassMatch;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

public interface InterceptorPointDefine {

  ClassMatch getEnhancedClass();

  List<InterceptorPoint> getInterceptorPointList();

  default DynamicType.Builder<?> define(TypeDescription typeDescription,
                                        DynamicType.Builder<?> builder, ClassLoader classLoader) {
    List<InterceptorPoint> interceptorPointList = getInterceptorPointList();
    for (InterceptorPoint interceptorPoint : interceptorPointList) {
      Interceptor interceptor = interceptorPoint.getInterceptor();
      if (interceptor.ifOverride()) {
        builder = builder
                .method(ElementMatchers.not(ElementMatchers.isStatic())
                        .and(interceptorPoint.getMethodsMatcher()))
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .withBinders(Morph.Binder.install(OverrideCallable.class))
                        .to(interceptor));
      } else {
        builder = builder
                .method(ElementMatchers.not(ElementMatchers.isStatic())
                        .and(interceptorPoint.getMethodsMatcher()))
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .to(interceptor));
      }
    }
    return builder;
  }

}
