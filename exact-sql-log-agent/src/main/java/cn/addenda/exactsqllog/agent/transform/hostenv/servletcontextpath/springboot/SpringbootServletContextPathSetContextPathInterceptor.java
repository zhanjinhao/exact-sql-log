package cn.addenda.exactsqllog.agent.transform.hostenv.servletcontextpath.springboot;

import cn.addenda.exactsqllog.agent.HostEnv;
import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.OverrideCallable;
import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

public class SpringbootServletContextPathSetContextPathInterceptor implements Interceptor {

  private static final SystemLogger systemLogger =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(SpringbootServletContextPathSetContextPathInterceptor.class);

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
          @Super Object druidDataSource,
          // 用于调用父类的方法。
          @Morph OverrideCallable zuper
  ) throws Exception {

    systemLogger.info("SpringbootServletContextPathSetContextPathInterceptor.class.classLoader = {}", this.getClass().getClassLoader());

    HostEnv.setServletContextPath((String) targetMethodArgs[0]);
    return zuper.call(targetMethodArgs);
  }

  @Override
  public boolean ifOverride() {
    return true;
  }

}
