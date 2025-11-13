package cn.addenda.exactsqllog.common.jvm;

public interface JVMShutdownCallback {

  void shutdown() throws Throwable;

  Integer getOrder();

}
