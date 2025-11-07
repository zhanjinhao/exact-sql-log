package cn.addenda.exactsqllog.proxy.test.jdbc;

import cn.addenda.exactsqllog.proxy.test.DbUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SpecialJDBCTest {

  private DataSource dataSource;

  @BeforeEach
  void before() {
    dataSource = DbUtils.getDataSource();
  }

  /**
   * PreparedStatement使用addBatch()和addBatch(String sql)之后，执行executeBatch()都能成功，且有序
   */
  @Test
  void testExecutionOrderOfSingleAndBatchMode() throws Exception {
    Connection connection = dataSource.getConnection();
    testExecutionOrderOfSingleAndBatchMode(connection);
  }

  protected void testExecutionOrderOfSingleAndBatchMode(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);

    preparedStatement.executeUpdate("insert into t_jdbc_test set name = 'SpecialJDBCTest-testExecutionOrderOfSingleAndBatchMode-0'");

    preparedStatement.addBatch("insert into t_jdbc_test set name = 'SpecialJDBCTest-testExecutionOrderOfSingleAndBatchMode-3'");

    preparedStatement.executeUpdate("insert into t_jdbc_test set name = 'SpecialJDBCTest-testExecutionOrderOfSingleAndBatchMode-1'");

    preparedStatement.setString(1, "SpecialJDBCTest-testExecutionOrderOfSingleAndBatchMode-4");

    preparedStatement.addBatch();

    preparedStatement.executeUpdate("insert into t_jdbc_test set name = 'SpecialJDBCTest-testExecutionOrderOfSingleAndBatchMode-2'");

    preparedStatement.addBatch("insert into t_jdbc_test set name = 'SpecialJDBCTest-testExecutionOrderOfSingleAndBatchMode-5'");

    int[] ints = preparedStatement.executeBatch();

    connection.commit();

    preparedStatement.close();
    connection.close();
  }

  /**
   * PreparedStatement的clearBatch()不会影响parameter，addBatch()依然可以使用这些参数
   */
  @Test
  void testClearBatchMethodDoesNotAffectParameters() throws Exception {
    Connection connection = dataSource.getConnection();
    testClearBatchMethodDoesNotAffectParameters(connection);
  }

  protected void testClearBatchMethodDoesNotAffectParameters(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);

    preparedStatement.addBatch("insert into t_jdbc_test set name = 'SpecialJDBCTest-testClearBatchMethodDoesNotAffectParameters-0'");

    preparedStatement.setString(1, "SpecialJDBCTest-testClearBatchMethodDoesNotAffectParameters-1");

    preparedStatement.addBatch();

    preparedStatement.setString(1, "SpecialJDBCTest-testClearBatchMethodDoesNotAffectParameters-2");

    preparedStatement.clearBatch();

    preparedStatement.addBatch();

    int[] ints = preparedStatement.executeBatch();

    connection.commit();

    preparedStatement.close();
    connection.close();
  }

  /**
   * PreparedStatement的clearParameters()不会影响addBatch()的数据
   */
  @Test
  void testClearParametersMethodDoesNotAffectDataThatHasAddBatched() throws Exception {
    Connection connection = dataSource.getConnection();
    testClearParametersMethodDoesNotAffectDataThatHasAddBatched(connection);
  }

  protected void testClearParametersMethodDoesNotAffectDataThatHasAddBatched(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);

    preparedStatement.addBatch("insert into t_jdbc_test set name = 'SpecialJDBCTest-testClearParametersMethodDoesNotAffectDataThatHasAddBatched-0'");

    preparedStatement.setString(1, "SpecialJDBCTest-testClearParametersMethodDoesNotAffectDataThatHasAddBatched-1");

    preparedStatement.addBatch();

    preparedStatement.clearParameters();

    preparedStatement.setString(1, "SpecialJDBCTest-testClearParametersMethodDoesNotAffectDataThatHasAddBatched-2");

    preparedStatement.addBatch();

    int[] ints = preparedStatement.executeBatch();

    connection.commit();

    preparedStatement.close();
    connection.close();
  }

  /**
   * close方法不会提交已执行未提交的数据
   */
  @Test
  void testCloseMethodDoesNotCommitDataThatHasExecutedButNotCommitted() throws Exception {
    Connection connection = dataSource.getConnection();
    testCloseMethodDoesNotCommitDataThatHasExecutedButNotCommitted(connection);
  }

  protected void testCloseMethodDoesNotCommitDataThatHasExecutedButNotCommitted(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);
    preparedStatement.setString(1, "SpecialJDBCTest-testCloseMethodDoesNotCommitDataThatHasExecutedButNotCommitted-0");
    preparedStatement.addBatch();
    preparedStatement.executeBatch();

    preparedStatement.close();

    connection.close();
  }

  /**
   * autoCommit是false，setAutoCommit为true，会提交数据
   */
  @Test
  void testCommitDataWhenUpdateAutoCommitFromFalseToTrue() throws Exception {
    Connection connection = dataSource.getConnection();
    testCommitDataWhenUpdateAutoCommitFromFalseToTrue(connection);
  }

  protected void testCommitDataWhenUpdateAutoCommitFromFalseToTrue(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);
    preparedStatement.setString(1, "SpecialJDBCTest-testCommitDataWhenUpdateAutoCommitFromFalseToTrue-0");
    preparedStatement.addBatch();
    preparedStatement.executeBatch();

    connection.setAutoCommit(true);

    preparedStatement.close();

    connection.close();
  }

  /**
   * autoCommit是false，setAutoCommit为false，不会提交数据
   */
  @Test
  void testCommitDataWhenUpdateAutoCommitFromFalseToFalse() throws Exception {
    Connection connection = dataSource.getConnection();
    testCommitDataWhenUpdateAutoCommitFromFalseToFalse(connection);
  }

  protected void testCommitDataWhenUpdateAutoCommitFromFalseToFalse(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);
    preparedStatement.setString(1, "SpecialJDBCTest-testCommitDataWhenUpdateAutoCommitFromFalseToFalse-0");
    preparedStatement.addBatch();
    preparedStatement.executeBatch();

    connection.setAutoCommit(false);

    preparedStatement.close();

    connection.close();
  }

  /**
   * execute但未提交的数据，clearBatch()后还在：clearBatch()只清理addBatch()的数据
   */
  @Test
  void testClearBatchMethodDoesNotClearDataThatHasExecuted() throws Exception {
    Connection connection = dataSource.getConnection();
    testClearBatchMethodDoesNotClearDataThatHasExecuted(connection);
  }

  protected void testClearBatchMethodDoesNotClearDataThatHasExecuted(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);
    preparedStatement.setString(1, "SpecialJDBCTest-testClearBatchMethodDoesNotClearDataThatHasExecuted-0");
    preparedStatement.addBatch();
    preparedStatement.executeBatch();

    preparedStatement.setString(1, "SpecialJDBCTest-testClearBatchMethodDoesNotClearDataThatHasExecuted-1");
    preparedStatement.addBatch();
    preparedStatement.clearBatch();
    preparedStatement.executeBatch();

    connection.commit();

    preparedStatement.close();

    connection.close();
  }

  /**
   * closeMethodDoesNotThrowExceptionInAnyExecutionOrder
   */
  @Test
  void testCloseMethodDoesNotThrowExceptionInAnyExecutionOrder() throws Exception {
    Connection connection = dataSource.getConnection();
    testCloseMethodDoesNotThrowExceptionInAnyExecutionOrder(connection);
  }

  protected void testCloseMethodDoesNotThrowExceptionInAnyExecutionOrder(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);

    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);
    preparedStatement.setString(1, "SpecialJDBCTest-closeMethodDoesNotThrowExceptionInAnyExecutionOrder-0");
    preparedStatement.addBatch();
    preparedStatement.executeBatch();

    connection.close();
    connection.close();

    preparedStatement.close();
    preparedStatement.close();
  }

  /**
   * addBatch()后不set再addBatch()，用相同的数据执行两次
   */
  @Test
  void testExecuteDoesNotClearParameter() throws Exception {
    Connection connection = dataSource.getConnection();
    testExecuteDoesNotClearParameter(connection);
  }

  protected void testExecuteDoesNotClearParameter(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(true);
    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(insetSql);
    preparedStatement.setString(1, "SpecialJDBCTest-testExecuteDoesNotClearParameter-0");
    preparedStatement.addBatch();
    preparedStatement.executeBatch();
    preparedStatement.addBatch();
    preparedStatement.executeBatch();

    preparedStatement.close();

    connection.close();
  }

  /**
   * ps1执行execute之后未commit就close了，等connection提交的时候，ps1执行的数据依然会提交
   */
  @Test
  void testPreparedStatementCloseMethodDoesNotDiscardData() throws Exception {
    Connection connection = dataSource.getConnection();
    testPreparedStatementCloseMethodDoesNotDiscardData(connection);
  }

  protected void testPreparedStatementCloseMethodDoesNotDiscardData(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);
    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement1 = connection.prepareStatement(insetSql);
    preparedStatement1.setString(1, "SpecialJDBCTest-testPreparedStatementCloseMethodDoesNotDiscardData-0");
    preparedStatement1.addBatch();
    preparedStatement1.executeBatch();

    PreparedStatement preparedStatement2 = connection.prepareStatement(insetSql);
    preparedStatement2.setString(1, "SpecialJDBCTest-testPreparedStatementCloseMethodDoesNotDiscardData-1");
    preparedStatement2.addBatch();
    preparedStatement2.executeBatch();

    preparedStatement1.close();

    connection.commit();

    preparedStatement2.close();

    connection.close();
  }

  /**
   * 所有execute的数据都会被rollback
   */
  @Test
  void testRollbackMethodWillRollBackAllDataOfPreparedStatement() throws Exception {
    Connection connection = dataSource.getConnection();
    testRollbackMethodWillRollBackAllDataOfPreparedStatement(connection);
  }

  protected void testRollbackMethodWillRollBackAllDataOfPreparedStatement(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);
    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement1 = connection.prepareStatement(insetSql);
    preparedStatement1.setString(1, "SpecialJDBCTest-testRollbackMethodWillRollBackAllDataOfPreparedStatement-0");
    preparedStatement1.addBatch();
    preparedStatement1.executeBatch();

    PreparedStatement preparedStatement2 = connection.prepareStatement(insetSql);
    preparedStatement2.setString(1, "SpecialJDBCTest-testRollbackMethodWillRollBackAllDataOfPreparedStatement-1");
    preparedStatement2.addBatch();
    preparedStatement2.executeBatch();

    preparedStatement1.close();

    connection.rollback();

    preparedStatement2.close();

    connection.close();
  }

  /**
   * rollback方法不会清理ps里未execute的数据
   */
  @Test
  void testRollbackMethodDoesNotClearDataThatHasNotExecuted() throws Exception {
    Connection connection = dataSource.getConnection();
    testRollbackMethodDoesNotClearDataThatHasNotExecuted(connection);
  }

  protected void testRollbackMethodDoesNotClearDataThatHasNotExecuted(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);
    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement1 = connection.prepareStatement(insetSql);
    preparedStatement1.setString(1, "SpecialJDBCTest-testRollbackMethodDoesNotClearDataThatHasNotExecuted-0");
    preparedStatement1.addBatch();

    PreparedStatement preparedStatement2 = connection.prepareStatement(insetSql);
    preparedStatement2.setString(1, "SpecialJDBCTest-testRollbackMethodDoesNotClearDataThatHasNotExecuted-1");
    preparedStatement2.addBatch();

    connection.rollback();

    preparedStatement1.executeBatch();
    preparedStatement2.executeBatch();

    connection.commit();

    preparedStatement1.clearParameters();
    preparedStatement2.clearParameters();

    connection.close();
  }

  /**
   * addBatch但是没有execute的sql，又change了autoCommit。不会提交。
   */
  @Test
  void testUpdateAutoCommitFromFalseToTrueDoesNotCommitDataThatHasAddBatched() throws Exception {
    Connection connection = dataSource.getConnection();
    testUpdateAutoCommitFromFalseToTrueDoesNotCommitDataThatHasAddBatched(connection);
  }

  protected void testUpdateAutoCommitFromFalseToTrueDoesNotCommitDataThatHasAddBatched(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);
    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement1 = connection.prepareStatement(insetSql);
    preparedStatement1.setString(1, "SpecialJDBCTest-testUpdateAutoCommitFromFalseToTrueDoesNotCommitDataThatHasAddBatched-0");
    preparedStatement1.addBatch();

    PreparedStatement preparedStatement2 = connection.prepareStatement(insetSql);
    preparedStatement2.setString(1, "SpecialJDBCTest-testUpdateAutoCommitFromFalseToTrueDoesNotCommitDataThatHasAddBatched-1");
    preparedStatement2.addBatch();

    connection.setAutoCommit(true);

    preparedStatement1.close();
    preparedStatement2.close();

    connection.close();
  }

  /**
   * 执行single方法不会执行batch方法
   */
  @Test
  void testExecuteUpdateDoesNotAffectDataThatHasAddBatched() throws Exception {
    Connection connection = dataSource.getConnection();
    testExecuteUpdateDoesNotAffectDataThatHasAddBatched(connection);
  }

  protected void testExecuteUpdateDoesNotAffectDataThatHasAddBatched(Connection connection) throws Exception {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    connection.setAutoCommit(false);
    String insetSql = "insert into t_jdbc_test set name = ?";

    PreparedStatement preparedStatement1 = connection.prepareStatement(insetSql);
    preparedStatement1.setString(1, "SpecialJDBCTest-testExecuteUpdateDoesNotAffectDataThatHasAddBatched-1");
    preparedStatement1.addBatch();

    preparedStatement1.addBatch("insert into t_jdbc_test set name = 'SpecialJDBCTest-testExecuteUpdateDoesNotAffectDataThatHasAddBatched-2'");

    PreparedStatement preparedStatement2 = connection.prepareStatement(insetSql);
    preparedStatement2.setString(1, "SpecialJDBCTest-testExecuteUpdateDoesNotAffectDataThatHasAddBatched-3");
    preparedStatement2.addBatch();

    preparedStatement1.executeUpdate("insert into t_jdbc_test set name = 'SpecialJDBCTest-testExecuteUpdateDoesNotAffectDataThatHasAddBatched-0'");

    preparedStatement1.executeBatch();
    preparedStatement2.executeBatch();

    preparedStatement1.executeBatch();
    preparedStatement2.executeBatch();

    preparedStatement1.close();
    preparedStatement2.close();

    connection.setAutoCommit(true);

    connection.close();
  }

  @AfterEach
  void after() throws Exception {
    if (dataSource instanceof AutoCloseable) {
      ((AutoCloseable) dataSource).close();
    }
  }

}
