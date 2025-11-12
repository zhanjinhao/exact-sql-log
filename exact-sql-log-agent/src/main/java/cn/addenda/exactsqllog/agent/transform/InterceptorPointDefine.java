package cn.addenda.exactsqllog.agent.transform;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

public interface InterceptorPointDefine {

  String getEnhancedClassName();

  List<InterceptorPoint> getInterceptorPointList();

  default DynamicType.Builder<?> define(TypeDescription typeDescription,
                                        DynamicType.Builder<?> builder, ClassLoader classLoader) {
    List<InterceptorPoint> interceptorPointList = getInterceptorPointList();
    for (InterceptorPoint interceptorPoint : interceptorPointList) {
      builder = builder
              .method(ElementMatchers.not(ElementMatchers.isStatic())
                      .and(interceptorPoint.getMethodsMatcher()))
              .intercept(MethodDelegation.withDefaultConfiguration()
                      .to(interceptorPoint.getInterceptor()));
    }
    return builder;
  }

}
