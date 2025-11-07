package cn.addenda.exactsqllog.proxy.test.esl;

import cn.addenda.exactsqllog.proxy.jdbc.EslDataSource;
import cn.addenda.exactsqllog.proxy.system.ProxyDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.proxy.test.DbUtils;
import cn.addenda.exactsqllog.proxy.test.jdbc.BatchModeJDBCTest;
import cn.addenda.exactsqllog.proxy.writer.ProxyDefaultSqlWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 批量执行场景的单元测试。对应{@link BatchModeJDBCTest}里的场景。
 */
class BatchModeEslTest extends BatchModeJDBCTest {

  private DataSource dataSource;

  @BeforeEach
  void before() {
    dataSource = DbUtils.getDataSource();
    dataSource = new EslDataSource(dataSource, ProxyDefaultSystemLoggerFactory.getInstance(), new ProxyDefaultSqlWriter());
  }

  @Test
  void test_statement_execute_batch_autoCommit_false_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_batch_autoCommit_false_commit(connection);
  }

  @Test
  void test_statement_execute_batch_autoCommit_false_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_batch_autoCommit_false_rollback(connection);
  }

  @Test
  void test_statement_execute_batch_autoCommit_true_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_batch_autoCommit_true_commit(connection);
  }

  @Test
  void test_statement_execute_batch_autoCommit_true_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_batch_autoCommit_true_rollback(connection);
  }

  @Test
  void test_preparedStatement_execute_batch_autoCommit_false_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch_autoCommit_false_commit(connection);
  }

  @Test
  void test_preparedStatement_execute_batch_autoCommit_false_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch_autoCommit_false_rollback(connection);
  }

  @Test
  void test_preparedStatement_execute_batch_autoCommit_true_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch_autoCommit_true_commit(connection);
  }

  @Test
  void test_preparedStatement_execute_batch_autoCommit_true_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch_autoCommit_true_rollback(connection);
  }

  @Test
  void test_preparedStatement_execute_batch2_autoCommit_false_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch2_autoCommit_false_commit(connection);
  }

  @Test
  void test_preparedStatement_execute_batch2_autoCommit_false_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch2_autoCommit_false_rollback(connection);
  }

  @Test
  void test_preparedStatement_execute_batch2_autoCommit_true_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch2_autoCommit_true_commit(connection);
  }

  @Test
  void test_preparedStatement_execute_batch2_autoCommit_true_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_batch2_autoCommit_true_rollback(connection);
  }

  @AfterEach
  void after() throws Exception {
    if (dataSource instanceof AutoCloseable) {
      ((AutoCloseable) dataSource).close();
    }
  }

}
