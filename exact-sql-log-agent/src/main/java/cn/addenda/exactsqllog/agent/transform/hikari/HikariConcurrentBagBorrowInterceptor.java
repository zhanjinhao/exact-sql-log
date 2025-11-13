package cn.addenda.exactsqllog.agent.transform.hikari;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.Interceptor;
import cn.addenda.exactsqllog.proxy.jdbc.EslConnection;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.concurrent.Callable;

public class HikariConcurrentBagBorrowInterceptor implements Interceptor {

  private Field _connectionField;

  private static final SystemLogger systemLogger =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(HikariConcurrentBagBorrowInterceptor.class);

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
    systemLogger.info("HikariPoolEntryRecycleInterceptor.class.classLoader = {}.", this.getClass().getClassLoader());

    Object call = zuper.call();
    if (call == null) {
      return call;
    }

    if (!"com.zaxxer.hikari.pool.PoolEntry".equals(call.getClass().getName())) {
      systemLogger.error("ConcurrentBag#borrow return [{}].", call.getClass().getName());
      return call;
    }

    Field connectionField = getConnectionField(call);
    connectionField.setAccessible(true);
    Connection connection = (Connection) connectionField.get(call);

    if (connection instanceof EslConnection) {
      ((EslConnection) connection).closeEsl();
    }

    return call;
  }

  private synchronized Field getConnectionField(Object o) throws Exception {
    if (_connectionField == null) {
      Class<?> _poolEntryClass = o.getClass();
      _connectionField = _poolEntryClass.getDeclaredField("connection");
    }
    return _connectionField;
  }

}
