package cn.addenda.exactsqllog.common.jvm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JVMShutdown {

  private final List<JVMShutdownCallback> jvmShutdownCallbackList = new ArrayList<>();

  private static final JVMShutdown instance = new JVMShutdown();

  public static JVMShutdown getInstance() {
    return instance;
  }

  private JVMShutdown() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        jvmShutdownCallbackList.sort(Comparator.comparing(JVMShutdownCallback::getOrder));
        for (JVMShutdownCallback jvmShutdownCallback : jvmShutdownCallbackList) {
          try {
            jvmShutdownCallback.shutdown();
          } catch (Throwable e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  public synchronized void addJvmShutdownCallback(JVMShutdownCallback jvmShutdownCallback) {
    jvmShutdownCallbackList.add(jvmShutdownCallback);
  }

}
