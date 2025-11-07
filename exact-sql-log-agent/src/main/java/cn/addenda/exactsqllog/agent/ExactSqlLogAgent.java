package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.AgentTransformer;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

/**
 * todo: 遗留问题，1、注册JVM回调钩子，清理资源。2、writer缓存，异步输出。3、拦截连接池的close方法，清理esl。
 */
public class ExactSqlLogAgent {

  private static final SystemLogger log = AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(ExactSqlLogAgent.class);

  public static void premain(String args, Instrumentation instrumentation) {

    log.info("ExactSqlLogAgent start enhancement, {}.class.classLoader = {}, args:{}",
            ExactSqlLogAgent.class.getName(), ExactSqlLogAgent.class.getClassLoader(), args);

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
            .type(new ElementMatcher<TypeDescription>() {
              @Override
              public boolean matches(TypeDescription target) {
                String typeName = target.getTypeName();
                return "com.mysql.cj.jdbc.NonRegisteringDriver".equals(typeName);
              }
            })
            .transform(new AgentTransformer())
            .with(new AgentListener());

    with.installOn(instrumentation);
  }

}
