package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springweb;

import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class SpringWebInterceptorPoint implements InterceptorPoint {

  public static final String MAPPING_PKG_PREFIX = "org.springframework.web.bind.annotation";
  public static final String MAPPING_SUFFIX = "Mapping";

  @Override
  public ElementMatcher<MethodDescription> getMethodsMatcher() {
    return ElementMatchers.not(ElementMatchers.isStatic())
            .and(ElementMatchers.isAnnotatedWith(
                            ElementMatchers.nameStartsWith(MAPPING_PKG_PREFIX)
                                    .and(ElementMatchers.nameEndsWith(MAPPING_SUFFIX))
                    )
            );
  }

  @Override
  public Interceptor getInterceptor() {
    return new SpringWebInterceptor();
  }

}
