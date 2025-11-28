package cn.addenda.exactsqllog.agent.test;

import cn.addenda.exactsqllog.common.entrypoint.EntryPoint;
import cn.addenda.exactsqllog.common.entrypoint.EntryPointType;
import cn.addenda.exactsqllog.proxy.entrypoint.EntryPointContext;
import org.junit.jupiter.api.Test;

class EntryPointContextTest {

  @Test
  void test1() {

    EntryPointContext.addEntryPoint(EntryPoint.of(EntryPointType.HTTP_SPRING, "1"));
    System.out.println(EntryPointContext.deepCopyAndGet());

    EntryPointContext.addEntryPoint(EntryPoint.of(EntryPointType.TX_TRANSACTIONAL, "2"));
    System.out.println(EntryPointContext.deepCopyAndGet());

    EntryPointContext.addEntryPoint(EntryPoint.of(EntryPointType.ORM_MYBATIS, "3"));
    System.out.println(EntryPointContext.deepCopyAndGet());


    EntryPointContext.removeEntryPoint(EntryPoint.of(EntryPointType.ORM_MYBATIS, "3"));
    System.out.println(EntryPointContext.deepCopyAndGet());

    EntryPointContext.removeEntryPoint(EntryPoint.of(EntryPointType.TX_TRANSACTIONAL, "2"));
    System.out.println(EntryPointContext.deepCopyAndGet());

    EntryPointContext.removeEntryPoint(EntryPoint.of(EntryPointType.HTTP_SPRING, "1"));
    System.out.println(EntryPointContext.deepCopyAndGet());

  }

}
