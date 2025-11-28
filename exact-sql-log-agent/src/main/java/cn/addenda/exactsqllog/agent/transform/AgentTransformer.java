package cn.addenda.exactsqllog.agent.transform;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefineGather;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;
import java.util.List;

public class AgentTransformer implements AgentBuilder.Transformer {

  private static final SystemLogger log =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(AgentTransformer.class);

  private final InterceptorPointDefineGather interceptorPointDefineGather;

  public AgentTransformer(InterceptorPointDefineGather interceptorPointDefineGather) {
    this.interceptorPointDefineGather = interceptorPointDefineGather;
  }

  @Override
  public DynamicType.Builder<?> transform(
          DynamicType.Builder<?> builder, TypeDescription typeDescription,
          ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {

    log.info("TypeName {} to Transform,  classLoader = {}", typeDescription.getTypeName(), classLoader);

    List<InterceptorPointDefine> interceptorPointDefineList = interceptorPointDefineGather.find(typeDescription);
    if (interceptorPointDefineList == null || interceptorPointDefineList.isEmpty()) {
      return builder;
    }

    DynamicType.Builder<?> newBuilder = builder;

    for (InterceptorPointDefine interceptorPointDefine : interceptorPointDefineList) {
      DynamicType.Builder<?> possibleNewBuilder =
              interceptorPointDefine.define(typeDescription, newBuilder, classLoader);
      // 这里使用的是链式调用，返回的不为空表示字节码被拦截增强了，可以作为下一次链式调用的对象
      if (possibleNewBuilder != null) {
        newBuilder = possibleNewBuilder;
      }
    }

    return newBuilder;
  }

}
