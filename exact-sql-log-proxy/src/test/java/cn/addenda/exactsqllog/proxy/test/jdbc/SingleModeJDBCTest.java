package cn.addenda.exactsqllog.proxy.test.jdbc;

import cn.addenda.exactsqllog.proxy.test.DbUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SingleModeJDBCTest {

  private DataSource dataSource;

  @BeforeEach
  void before() {
    dataSource = DbUtils.getDataSource();
  }

  @Test
  void test_statement_execute_update_autoCommit_false_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_update_autoCommit_false_commit(connection);
  }

  protected void test_statement_execute_update_autoCommit_false_commit(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    Statement statement = connection.createStatement();

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_false_commit-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_false_commit-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_false_commit-2'");

    connection.commit();

    statement.close();
    connection.close();
  }

  @Test
  void test_statement_execute_update_autoCommit_false_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_update_autoCommit_false_rollback(connection);
  }

  protected void test_statement_execute_update_autoCommit_false_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    Statement statement = connection.createStatement();

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_false_rollback-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_false_rollback-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_false_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @Test
  void test_statement_execute_update_autoCommit_true_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_update_autoCommit_true_commit(connection);
  }

  protected void test_statement_execute_update_autoCommit_true_commit(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    Statement statement = connection.createStatement();

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_true_commit-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_true_commit-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_true_commit-2'");

    statement.close();
    connection.close();
  }

  @Test
  void test_statement_execute_update_autoCommit_true_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_update_autoCommit_true_rollback(connection);
  }

  protected void test_statement_execute_update_autoCommit_true_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    Statement statement = connection.createStatement();

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_true_rollback-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_true_rollback-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_statement_execute_update_autoCommit_true_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @Test
  void test_statement_execute_query_autoCommit_false_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_query_autoCommit_false_commit(connection);
  }

  protected void test_statement_execute_query_autoCommit_false_commit(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    Statement statement = connection.createStatement();

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_false_commit-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_false_commit-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_false_commit-2'");

    connection.commit();

    statement.close();
    connection.close();
  }

  @Test
  void test_statement_execute_query_autoCommit_false_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_query_autoCommit_false_rollback(connection);
  }

  protected void test_statement_execute_query_autoCommit_false_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    Statement statement = connection.createStatement();

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_false_rollback-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_false_rollback-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_false_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @Test
  void test_statement_execute_query_autoCommit_true_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_query_autoCommit_true_commit(connection);
  }

  protected void test_statement_execute_query_autoCommit_true_commit(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    Statement statement = connection.createStatement();

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_true_commit-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_true_commit-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_true_commit-2'");

    statement.close();
    connection.close();
  }

  @Test
  void test_statement_execute_query_autoCommit_true_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_statement_execute_query_autoCommit_true_rollback(connection);
  }

  protected void test_statement_execute_query_autoCommit_true_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    Statement statement = connection.createStatement();

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_true_rollback-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_true_rollback-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_statement_execute_query_autoCommit_true_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_update_autoCommit_false_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_update_autoCommit_false_commit(connection);
  }

  protected void test_preparedStatement_execute_update_autoCommit_false_commit(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_false_commit-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_false_commit-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_false_commit-2'");

    connection.commit();

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_update_autoCommit_false_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_update_autoCommit_false_rollback(connection);
  }

  protected void test_preparedStatement_execute_update_autoCommit_false_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_false_rollback-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_false_rollback-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_false_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_update_autoCommit_true_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_update_autoCommit_true_commit(connection);
  }

  protected void test_preparedStatement_execute_update_autoCommit_true_commit(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_true_commit-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_true_commit-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_true_commit-2'");

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_update_autoCommit_true_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_update_autoCommit_true_rollback(connection);
  }

  protected void test_preparedStatement_execute_update_autoCommit_true_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_true_rollback-0'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_true_rollback-1'");
    statement.executeUpdate("insert into t_jdbc_test set name = 'SingleModeJDBCTest-test_preparedStatement_execute_update_autoCommit_true_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_query_autoCommit_false_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_query_autoCommit_false_commit(connection);
  }

  protected void test_preparedStatement_execute_query_autoCommit_false_commit(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_false_commit-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_false_commit-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_false_commit-2'");

    connection.commit();

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_query_autoCommit_false_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_query_autoCommit_false_rollback(connection);
  }

  protected void test_preparedStatement_execute_query_autoCommit_false_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(false);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_false_rollback-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_false_rollback-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_false_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_query_autoCommit_true_commit() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_query_autoCommit_true_commit(connection);
  }

  protected void test_preparedStatement_execute_query_autoCommit_true_commit(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_true_commit-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_true_commit-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_true_commit-2'");

    statement.close();
    connection.close();
  }

  @Test
  void test_preparedStatement_execute_query_autoCommit_true_rollback() throws Exception {
    Connection connection = dataSource.getConnection();
    test_preparedStatement_execute_query_autoCommit_true_rollback(connection);
  }

  protected void test_preparedStatement_execute_query_autoCommit_true_rollback(Connection connection) throws Exception {
    connection.setAutoCommit(true);
    PreparedStatement statement = connection.prepareStatement("insert into t_jdbc_test");

    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_true_rollback-0'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_true_rollback-1'");
    statement.executeQuery("select * from t_jdbc_test where name = 'SingleModeJDBCTest-test_preparedStatement_execute_query_autoCommit_true_rollback-2'");

    connection.rollback();

    statement.close();
    connection.close();
  }

  @AfterEach
  void after() throws Exception {
    if (dataSource instanceof AutoCloseable) {
      ((AutoCloseable) dataSource).close();
    }
  }

}
