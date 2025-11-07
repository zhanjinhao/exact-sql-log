package cn.addenda.exactsqllog.proxy.test.log4j2;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.junit.jupiter.api.Test;

@Slf4j
class Log4j2FileTest {

  @Test
  void test1() {
    for (int i = 0; i < 10000; i++) {
      log.info("1234567890qwertyuiopasdfghjklzxcvbnm : {}", i);
    }
  }

  @Test
  void test2() {
    for (int i = 0; i < 10000; i++) {
      log.error("1234567890qwertyuiopasdfghjklzxcvbnm : {}", i);
    }
  }

  @Test
  void test3() {
    LoggerContext context = LoggerContext.getContext();
    ExtendedLogger logger = context.getLogger(Log4j2FileTest.class);
    ExtendedLogger logger2 = context.getLogger("");

  }


}
