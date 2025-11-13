package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.AgentTransformer;
import cn.addenda.exactsqllog.agent.transform.InterceptorPointDefineGather;
import cn.addenda.exactsqllog.agent.transform.druid.DruidDruidDataSourceInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.hikari.HikariConcurrentBagInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.mysql.MySQLDriverInterceptorPointDefine;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

/**
 * todo: 遗留问题，
 * 1、RuntimeException 改为自定义异常
 * 2、拦截接口的进入点，事务的进入点，orm的进入点
 * <p>
 * com.alibaba.druid.pool.DruidConnectionHolder#reset()
 * <p>
 * com.zaxxer.hikari.pool.PoolEntry#(long)
 */
public class ExactSqlLogAgent {

  private static final SystemLogger log = AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(ExactSqlLogAgent.class);

  public static void premain(String args, Instrumentation instrumentation) {

    log.info("ExactSqlLogAgent start enhancement, {}.class.classLoader = {}, args:{}",
            ExactSqlLogAgent.class.getName(), ExactSqlLogAgent.class.getClassLoader(), args);

    InterceptorPointDefineGather interceptorPointDefineGather = getInterceptorGather();

    ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.of(true));

    AgentBuilder with = new AgentBuilder.Default(byteBuddy)
            .ignore(
                    nameStartsWith("net.bytebuddy.")
                            .or(nameStartsWith("org.springframework."))
                            .or(nameStartsWith("org.slf4j."))
                            .or(nameStartsWith("org.groovy."))
                            .or(nameContains("javassist"))
                            .or(nameContains(".asm."))
                            .or(nameContains(".reflectasm."))
                            .or(nameStartsWith("sun.reflect"))
                            .or(ElementMatchers.isSynthetic()))
            // 当要被拦截的type第一次要被加载的时候会进入这里
            .type(interceptorPointDefineGather.buildMatch())
            .transform(new AgentTransformer(interceptorPointDefineGather))
            .with(new AgentListener());

    with.installOn(instrumentation);
  }

  private static InterceptorPointDefineGather getInterceptorGather() {
    InterceptorPointDefineGather interceptorPointDefineGather = new InterceptorPointDefineGather();
    interceptorPointDefineGather.addInterceptorPointDefine(new MySQLDriverInterceptorPointDefine());
    interceptorPointDefineGather.addInterceptorPointDefine(new HikariConcurrentBagInterceptorPointDefine());
    interceptorPointDefineGather.addInterceptorPointDefine(new DruidDruidDataSourceInterceptorPointDefine());
    return interceptorPointDefineGather;
  }

}
