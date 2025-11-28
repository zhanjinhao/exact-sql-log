package cn.addenda.exactsqllog.common.test;

import cn.addenda.exactsqllog.common.entrypoint.EntryPoint;
import cn.addenda.exactsqllog.common.entrypoint.EntryPointQueue;
import cn.addenda.exactsqllog.common.entrypoint.EntryPointType;
import org.junit.jupiter.api.Test;

class EntryPointQueueTest {

  @Test
  void test1() {
    EntryPointQueue entryPointQueue = new EntryPointQueue();

    entryPointQueue.trySet(EntryPoint.of(EntryPointType.HTTP_SPRING, "1"));
    System.out.println(entryPointQueue);
    System.out.println(entryPointQueue.ifEmpty());

    entryPointQueue.trySet(EntryPoint.of(EntryPointType.ORM_MYBATIS, "2"));
    System.out.println(entryPointQueue);
    System.out.println(entryPointQueue.ifEmpty());

    entryPointQueue.remove(EntryPoint.of(EntryPointType.ORM_MYBATIS, "2"));
    System.out.println(entryPointQueue);
    System.out.println(entryPointQueue.ifEmpty());
  }

}
