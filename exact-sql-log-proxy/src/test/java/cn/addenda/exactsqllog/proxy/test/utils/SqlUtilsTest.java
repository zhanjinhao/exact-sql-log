package cn.addenda.exactsqllog.proxy.test.utils;

import cn.addenda.exactsqllog.proxy.util.SqlUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqlUtilsTest {

  @Test
  void test1() {
    Assertions.assertEquals(true, SqlUtils.ifQuerySql("select * from t"));
    Assertions.assertEquals(true, SqlUtils.ifQuerySql(" select * from t"));
    Assertions.assertEquals(true, SqlUtils.ifQuerySql(" sElect  * from t"));
    Assertions.assertEquals(false, SqlUtils.ifQuerySql(" update t set id = id"));
    Assertions.assertEquals(false, SqlUtils.ifQuerySql(" delete from t where id = ?"));
    Assertions.assertEquals(false, SqlUtils.ifQuerySql(" insert into t set id = 1"));
  }

}
