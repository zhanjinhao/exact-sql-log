package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springweb;

import cn.addenda.exactsqllog.agent.HostEnv;
import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.common.entrypoint.EntryPoint;
import cn.addenda.exactsqllog.common.entrypoint.EntryPointType;
import cn.addenda.exactsqllog.proxy.entrypoint.EntryPointContext;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springweb.SpringWebInterceptorPoint.MAPPING_PKG_PREFIX;
import static cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springweb.SpringWebInterceptorPoint.MAPPING_SUFFIX;

public class SpringWebInterceptor implements Interceptor {

  private static final SystemLogger log =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(SpringWebInterceptor.class);

  /**
   * 被@RuntimeType标注的方法就是被委托的方法
   */
  @RuntimeType
  public Object intercept(
          // byteBuddy会在运行期间给被注定注解修饰的方法参数进行赋值:

          // 当前被拦截的、动态生成的那个对象
          @This Object targetObj,
          // 被调用的原始方法
          @Origin Method targetMethod,
          // 被拦截的方法参数
          @AllArguments Object[] targetMethodArgs,
          // 当前被拦截的、动态生成的那个对象的父类对象
          @Super Object concurrentBag,
          // 用于调用父类的方法。
          @SuperCall Callable<?> zuper
  ) throws Exception {
    log.info("SpringWebInterceptor.class.classLoader = {}.", this.getClass().getClassLoader());

    Annotation[] classAnnotations = targetObj.getClass().getAnnotations();

    Annotation[] methodAnnotations = targetMethod.getAnnotations();

    EntryPoint entryPoint = EntryPoint.of(
            EntryPointType.HTTP_SPRING, HostEnv.getServletContextPath() + getMappingValue(classAnnotations) + getMappingValue(methodAnnotations));
    EntryPointContext.addEntryPoint(entryPoint);

    try {
      return zuper.call();
    } finally {
      EntryPointContext.removeEntryPoint(entryPoint);
    }

  }

  private String getMappingValue(Annotation[] annotations) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    if (annotations == null || annotations.length == 0) {
      return "";
    }
    for (Annotation annotation : annotations) {
      String name = annotation.annotationType().getName();
      if (name.startsWith(MAPPING_PKG_PREFIX) && name.endsWith(MAPPING_SUFFIX)) {
        Method valueMethod = annotation.annotationType().getMethod("value");
        String[] values = (String[]) valueMethod.invoke(annotation);
        if (values != null && values.length > 0) {
          return "/" + values[0];  // 返回第一个value
        }
      }
    }
    return "";
  }

  @Override
  public boolean ifOverride() {
    return false;
  }

}
