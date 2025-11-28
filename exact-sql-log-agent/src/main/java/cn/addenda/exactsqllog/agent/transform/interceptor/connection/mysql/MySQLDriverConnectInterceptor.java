package cn.addenda.exactsqllog.agent.transform.interceptor.connection.mysql;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.agent.transform.interceptor.connection.hikari.HikariConcurrentBagBorrowInterceptor;
import cn.addenda.exactsqllog.agent.writer.AgentChainSqlWriter;
import cn.addenda.exactsqllog.common.config.EslConnectionConfig;
import cn.addenda.exactsqllog.proxy.jdbc.EslConnection;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.Callable;

public class MySQLDriverConnectInterceptor implements Interceptor {

  private final SqlWriter sqlWriter;

  private final SystemLogger elsConnectionSystemLogger;

  private static final SystemLogger systemLogger =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(HikariConcurrentBagBorrowInterceptor.class);

  public MySQLDriverConnectInterceptor() {
    this.sqlWriter = new AgentChainSqlWriter();
    this.elsConnectionSystemLogger = AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(EslConnection.class);
  }

  /**
   * 被@RuntimeType标注的方法就是被委托的方法
   */
  @RuntimeType
  public Connection intercept(
          // byteBuddy会在运行期间给被注定注解修饰的方法参数进行赋值:

          // 当前被拦截的、动态生成的那个对象
          @This Object targetObj,
          // 被调用的原始方法
          @Origin Method targetMethod,
          // 被拦截的方法参数
          @AllArguments Object[] targetMethodArgs,
          // 当前被拦截的、动态生成的那个对象的父类对象
          @Super Object originalObj,
          // 用于调用父类的方法。
          @SuperCall Callable<?> zuper
  ) throws Exception {

    systemLogger.info("MySQLDriverConnectInterceptor.class.classLoader = {}", this.getClass().getClassLoader());

    Object call = zuper.call();

    EslConnection eslConnection = new EslConnection(
            (Connection) call, elsConnectionSystemLogger, sqlWriter);

    systemLogger.info("EslConnection.class.classLoader = {}", eslConnection.getClass().getClassLoader());

    EslConnectionConfig eslConnectionConfig = new EslConnectionConfig();
    eslConnectionConfig.setJdbcUrl((String) targetMethodArgs[0]);
    eslConnectionConfig.setUser(((Properties) targetMethodArgs[1]).getProperty("user"));
    eslConnectionConfig.setPassword(((Properties) targetMethodArgs[1]).getProperty("password"));
    eslConnectionConfig.setEslConnectionId(eslConnection.getEslId());

    sqlWriter.logEslConnectionConfig(eslConnectionConfig);

    return eslConnection;
  }

  @Override
  public boolean ifOverride() {
    return false;
  }

}
