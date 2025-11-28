package cn.addenda.exactsqllog.proxy.entrypoint;

import cn.addenda.exactsqllog.common.entrypoint.EntryPoint;
import cn.addenda.exactsqllog.common.entrypoint.EntryPointQueue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntryPointContext {

  private static final ThreadLocal<EntryPointQueue> tl = ThreadLocal.withInitial(EntryPointQueue::new);

  public static void addEntryPoint(EntryPoint entryPoint) {
    EntryPointQueue entryPointQueue = tl.get();
    entryPointQueue.trySet(entryPoint);
  }

  public static void removeEntryPoint(EntryPoint entryPoint) {
    EntryPointQueue entryPointQueue = tl.get();
    entryPointQueue.remove(entryPoint);
    if (entryPointQueue.ifEmpty()) {
      tl.remove();
    }
  }

  public static EntryPointQueue get() {
    return tl.get();
  }

  public static EntryPointQueue deepCopyAndGet() {
    EntryPointQueue entryPointQueue = get();
    if (entryPointQueue == null) {
      return null;
    }
    return entryPointQueue.deepCopy();
  }

}
