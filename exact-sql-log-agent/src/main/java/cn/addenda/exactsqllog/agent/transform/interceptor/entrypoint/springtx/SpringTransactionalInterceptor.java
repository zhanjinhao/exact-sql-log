package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springtx;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.common.entrypoint.EntryPoint;
import cn.addenda.exactsqllog.common.entrypoint.EntryPointType;
import cn.addenda.exactsqllog.proxy.entrypoint.EntryPointContext;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class SpringTransactionalInterceptor implements Interceptor {

  private static final SystemLogger log =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(SpringTransactionalInterceptor.class);

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
    log.info("SpringTransactionalInterceptor.class.classLoader = {}.", this.getClass().getClassLoader());

    EntryPoint entryPoint = EntryPoint.of(
            EntryPointType.TX_TRANSACTIONAL,
            targetObj.getClass().getName() + "#" + targetMethod.getName());
    EntryPointContext.addEntryPoint(entryPoint);

    try {
      return zuper.call();
    } finally {
      EntryPointContext.removeEntryPoint(entryPoint);
    }

  }

  @Override
  public boolean ifOverride() {
    return false;
  }

}
