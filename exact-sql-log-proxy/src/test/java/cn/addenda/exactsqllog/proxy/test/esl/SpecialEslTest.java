package cn.addenda.exactsqllog.proxy.test.esl;

import cn.addenda.exactsqllog.proxy.jdbc.EslDataSource;
import cn.addenda.exactsqllog.proxy.system.ProxyDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.proxy.test.DbUtils;
import cn.addenda.exactsqllog.proxy.test.jdbc.SpecialJDBCTest;
import cn.addenda.exactsqllog.proxy.writer.ProxyDefaultSqlWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 特殊执行场景的单元测试。对应{@link SpecialJDBCTest}里的场景。
 */
class SpecialEslTest extends SpecialJDBCTest {

  private DataSource dataSource;

  @BeforeEach
  void before() {
    dataSource = DbUtils.getDataSource();
    dataSource = new EslDataSource(dataSource, ProxyDefaultSystemLoggerFactory.getInstance(), new ProxyDefaultSqlWriter());
  }

  @Test
  void testExecutionOrderOfSingleAndBatchMode() throws Exception {
    Connection connection = dataSource.getConnection();
    testExecutionOrderOfSingleAndBatchMode(connection);
  }

  @Test
  void testClearBatchMethodDoesNotAffectParameters() throws Exception {
    Connection connection = dataSource.getConnection();
    testClearBatchMethodDoesNotAffectParameters(connection);
  }

  @Test
  void testClearParametersMethodDoesNotAffectDataThatHasAddBatched() throws Exception {
    Connection connection = dataSource.getConnection();
    testClearParametersMethodDoesNotAffectDataThatHasAddBatched(connection);
  }

  @Test
  void testCloseMethodDoesNotCommitDataThatHasExecutedButNotCommitted() throws Exception {
    Connection connection = dataSource.getConnection();
    testCloseMethodDoesNotCommitDataThatHasExecutedButNotCommitted(connection);
  }

  @Test
  void testCommitDataWhenUpdateAutoCommitFromFalseToTrue() throws Exception {
    Connection connection = dataSource.getConnection();
    testCommitDataWhenUpdateAutoCommitFromFalseToTrue(connection);
  }

  @Test
  void testCommitDataWhenUpdateAutoCommitFromFalseToFalse() throws Exception {
    Connection connection = dataSource.getConnection();
    testCommitDataWhenUpdateAutoCommitFromFalseToFalse(connection);
  }

  @Test
  void testClearBatchMethodDoesNotClearDataThatHasExecuted() throws Exception {
    Connection connection = dataSource.getConnection();
    testClearBatchMethodDoesNotClearDataThatHasExecuted(connection);
  }

  @Test
  void testCloseMethodDoesNotThrowExceptionInAnyExecutionOrder() throws Exception {
    Connection connection = dataSource.getConnection();
    testCloseMethodDoesNotThrowExceptionInAnyExecutionOrder(connection);
  }

  @Test
  void testExecuteDoesNotClearParameter() throws Exception {
    Connection connection = dataSource.getConnection();
    testExecuteDoesNotClearParameter(connection);
  }

  @Test
  void testPreparedStatementCloseMethodDoesNotDiscardData() throws Exception {
    Connection connection = dataSource.getConnection();
    testPreparedStatementCloseMethodDoesNotDiscardData(connection);
  }

  @Test
  void testRollbackMethodWillRollBackAllDataOfPreparedStatement() throws Exception {
    Connection connection = dataSource.getConnection();
    testRollbackMethodWillRollBackAllDataOfPreparedStatement(connection);
  }

  @Test
  void testRollbackMethodDoesNotClearDataThatHasNotExecuted() throws Exception {
    Connection connection = dataSource.getConnection();
    testRollbackMethodDoesNotClearDataThatHasNotExecuted(connection);
  }

  @Test
  void testUpdateAutoCommitFromFalseToTrueDoesNotCommitDataThatHasAddBatched() throws Exception {
    Connection connection = dataSource.getConnection();
    testUpdateAutoCommitFromFalseToTrueDoesNotCommitDataThatHasAddBatched(connection);
  }

  @Test
  void testExecuteUpdateDoesNotAffectDataThatHasAddBatched() throws Exception {
    Connection connection = dataSource.getConnection();
    testExecuteUpdateDoesNotAffectDataThatHasAddBatched(connection);
  }

  @AfterEach
  void after() throws Exception {
    if (dataSource instanceof AutoCloseable) {
      ((AutoCloseable) dataSource).close();
    }
  }

}
