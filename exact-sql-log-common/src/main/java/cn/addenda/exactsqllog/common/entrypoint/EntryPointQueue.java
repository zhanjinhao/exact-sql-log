package cn.addenda.exactsqllog.common.entrypoint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class EntryPointQueue {

  private int capacity = 0;

  private final List<EntryPoint> list = new ArrayList<>();

  public void forceSet(EntryPoint entryPoint) {
    doSet(entryPoint, true);
  }

  public void trySet(EntryPoint entryPoint) {
    doSet(entryPoint, false);
  }

  private void doSet(EntryPoint entryPoint, boolean override) {
    Integer level = entryPoint.getEntryPointType().getLevel();
    expand(level);
    int index = level - 1;
    EntryPoint oldEntryPoint = list.get(index);
    if (override || oldEntryPoint == null) {
      list.set(index, entryPoint);
    }
  }

  public void remove(EntryPoint entryPoint) {
    Integer level = entryPoint.getEntryPointType().getLevel();
    expand(level);
    int index = level - 1;
    list.set(index, null);
    collapse();
  }

  private void expand(Integer level) {
    if (level > capacity) {
      for (int i = capacity; i < level; i++) {
        list.add(null);
      }
      capacity = level;
    }
  }

  private void collapse() {
    int i = capacity - 1;
    while (i > -1) {
      if (list.get(i) == null) {
        list.remove(i);
        i--;
      } else {
        break;
      }
    }
    capacity = i + 1;
  }

  public boolean ifEmpty() {
    if (list.isEmpty()) {
      return true;
    }
    // 这里循环不太会消耗性能。
    // Context退栈的时候，最后退index为0的数据。
    // 所以第一个就能判断是否是空。
    for (int i = 0; i < capacity; i++) {
      if (list.get(i) != null) {
        return false;
      }
    }
    return true;
  }

  public EntryPointQueue deepCopy() {
    EntryPointQueue entryPointQueue = new EntryPointQueue();
    entryPointQueue.capacity = capacity;
    for (int i = 0; i < capacity; i++) {
      EntryPoint entryPoint = list.get(i);
      if (entryPoint == null) {
        entryPointQueue.list.add(null);
      } else {
        entryPointQueue.list.add(EntryPoint.of(entryPoint));
      }
    }
    return entryPointQueue;
  }

}
