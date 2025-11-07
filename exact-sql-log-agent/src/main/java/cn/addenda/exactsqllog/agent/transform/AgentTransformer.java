package cn.addenda.exactsqllog.agent.transform;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class AgentTransformer implements AgentBuilder.Transformer {

  private static final SystemLogger log = AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(AgentTransformer.class);

  @Override
  public DynamicType.Builder<?> transform(
          DynamicType.Builder<?> builder, TypeDescription typeDescription,
          ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {

    log.info("TypeName {} to Transform,  classLoader = {}", typeDescription.getTypeName(), classLoader);

    builder = builder.method(ElementMatchers.named("connect"))
            .intercept(MethodDelegation.withDefaultConfiguration()
                    .to(new DriverConnectInterceptor()));

    return builder;
  }

}
