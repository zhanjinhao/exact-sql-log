package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.AgentTransformer;
import cn.addenda.exactsqllog.agent.transform.hostenv.servletcontextpath.springboot.SpringbootServletContextPathInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefineGather;
import cn.addenda.exactsqllog.agent.transform.interceptor.connection.druid.DruidDruidDataSourceInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.connection.hikari.HikariConcurrentBagInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.connection.mysql.MySQLDriverInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.eslstatement.EslStatementInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.mybatis.MybatisExecutorInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springtx.SpringTransactionalInterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springweb.SpringWebInterceptorPointDefine;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

import static cn.addenda.exactsqllog.agent.transform.hostenv.servletcontextpath.springboot.SpringbootServletContextPathInterceptorPointDefine.SERVER_PROPERTIES_SERVLET_NAME;
import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class ExactSqlLogAgent {

  public static void premain(String args, Instrumentation instrumentation) {

    addBootLibToBootstrapClassLoaderSearch(instrumentation);

    SystemLogger log = AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(ExactSqlLogAgent.class);

    log.info("ExactSqlLogAgent start enhancement, {}.class.classLoader = {}, args:{}",
            ExactSqlLogAgent.class.getName(), ExactSqlLogAgent.class.getClassLoader(), args);

    InterceptorPointDefineGather interceptorPointDefineGather = getInterceptorGather();

    ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.of(true));

    AgentBuilder with = new AgentBuilder.Default(byteBuddy)
            .ignore(
                    nameStartsWith("net.bytebuddy.")
                            .or(nameStartsWith("org.springframework.")
                                    .and(ElementMatchers.not(ElementMatchers.named(SERVER_PROPERTIES_SERVLET_NAME))))
                            .or(nameStartsWith("org.slf4j."))
                            .or(nameStartsWith("org.groovy."))
                            .or(nameContains("javassist"))
                            .or(nameContains(".asm."))
                            .or(nameContains(".reflectasm."))
                            .or(nameStartsWith("sun.reflect"))
                            .or(ElementMatchers.isSynthetic())
            )
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
    interceptorPointDefineGather.addInterceptorPointDefine(new SpringWebInterceptorPointDefine());
    interceptorPointDefineGather.addInterceptorPointDefine(new MybatisExecutorInterceptorPointDefine());
    interceptorPointDefineGather.addInterceptorPointDefine(new SpringTransactionalInterceptorPointDefine());
    interceptorPointDefineGather.addInterceptorPointDefine(new SpringbootServletContextPathInterceptorPointDefine());
    interceptorPointDefineGather.addInterceptorPointDefine(new EslStatementInterceptorPointDefine());
    return interceptorPointDefineGather;
  }

  private static void addBootLibToBootstrapClassLoaderSearch(Instrumentation instrumentation) {
    File bootDir = new File(AgentPackage.getPath(), "boot");
    File facadeDir = new File(bootDir, "facade");
    File commonDir = new File(bootDir, "common");

    addBootLibToBootstrapClassLoaderSearch(instrumentation, facadeDir);
    addBootLibToBootstrapClassLoaderSearch(instrumentation, commonDir);
  }

  private static void addBootLibToBootstrapClassLoaderSearch(Instrumentation instrumentation, File dir) {
    File[] bootLibs = AgentPackage.findJarFiles(dir);
    for (File bootLib : bootLibs) {
      try {
        instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(bootLib));
      } catch (IOException e) {
        throw new ExactSqlLogAgentBootstrapException(String.format("cannot append %s to BootstrapClassLoaderSearch", bootLib.getAbsolutePath()), e);
      }
    }
  }

}
