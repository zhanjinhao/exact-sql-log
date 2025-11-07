package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

public class AgentListener implements AgentBuilder.Listener {

  private static final SystemLogger log = AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(AgentListener.class);

  @Override
  public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

  }

  @Override
  public void onTransformation(final TypeDescription typeDescription,
                               final ClassLoader classLoader,
                               final JavaModule module,
                               final boolean loaded,
                               final DynamicType dynamicType) {

    InstrumentDebuggingClass.INSTANCE.log(dynamicType);
  }

  @Override
  public void onIgnored(final TypeDescription typeDescription,
                        final ClassLoader classLoader,
                        final JavaModule module,
                        final boolean loaded) {

  }

  @Override
  public void onError(final String typeName,
                      final ClassLoader classLoader,
                      final JavaModule module,
                      final boolean loaded,
                      final Throwable throwable) {
    log.error("Enhance class " + typeName + " error.", throwable);
  }

  @Override
  public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
  }

}
