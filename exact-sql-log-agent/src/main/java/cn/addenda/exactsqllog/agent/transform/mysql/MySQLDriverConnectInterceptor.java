package cn.addenda.exactsqllog.agent.transform.mysql;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.Interceptor;
import cn.addenda.exactsqllog.agent.writer.AgentChainSqlWriter;
import cn.addenda.exactsqllog.agent.writer.AgentHttpSqlWriter;
import cn.addenda.exactsqllog.agent.writer.AgentLogSqlWriter;
import cn.addenda.exactsqllog.proxy.jdbc.EslConnection;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.concurrent.Callable;

public class MySQLDriverConnectInterceptor implements Interceptor {

  private final SqlWriter sqlWriter;

  private final SystemLogger elsConnectionSystemLogger;

  public MySQLDriverConnectInterceptor() {
    AgentLogSqlWriter agentLogSqlWriter = new AgentLogSqlWriter();
    AgentHttpSqlWriter agentHttpSqlWriter = new AgentHttpSqlWriter();
    this.sqlWriter = new AgentChainSqlWriter(agentLogSqlWriter, agentHttpSqlWriter);
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
          // todo 要改成自定义的Callable，用于拦截账户密码
          @SuperCall Callable<?> zuper
  ) throws Exception {

    System.out.println("MySQLDriverConnectInterceptor.class.classLoader = " + this.getClass().getClassLoader());

    Object call = zuper.call();

    EslConnection eslConnection = new EslConnection(
            (Connection) call, elsConnectionSystemLogger, sqlWriter);

    System.out.println("EslConnection.class.classLoader = " + eslConnection.getClass().getClassLoader());

    return eslConnection;
  }

}
