package cn.addenda.exactsqllog.proxy.jdbc;

import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.common.util.UuidUtils;
import cn.addenda.exactsqllog.proxy.sql.EslStatementSqlAttachment;
import cn.addenda.exactsqllog.proxy.sql.ExecutionQueue;
import cn.addenda.exactsqllog.proxy.util.SqlUtils;

import java.sql.*;

/**
 * @author addenda
 * @since 2024/8/24 20:41
 */
public abstract class AbstractEslStatement<T extends Statement, P extends EslStatementSqlAttachment>
        extends WrapperAdapter implements Statement, EslIded {

  protected final T delegate;

  private final EslConnection eslConnection;

  protected P eslStatementSqlAttachment;

  private final String eslId;

  protected AbstractEslStatement(T delegate, EslConnection eslConnection) {
    this.delegate = delegate;
    this.eslConnection = eslConnection;
    this.eslId = UuidUtils.generateUuid();
  }

  @Override
  public int getMaxFieldSize() throws SQLException {
    return delegate.getMaxFieldSize();
  }

  @Override
  public void setMaxFieldSize(int max) throws SQLException {
    delegate.setMaxFieldSize(max);
  }

  @Override
  public int getMaxRows() throws SQLException {
    return delegate.getMaxRows();
  }

  @Override
  public void setMaxRows(int max) throws SQLException {
    delegate.setMaxRows(max);
  }

  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException {
    delegate.setEscapeProcessing(enable);
  }

  @Override
  public int getQueryTimeout() throws SQLException {
    return delegate.getQueryTimeout();
  }

  @Override
  public void setQueryTimeout(int seconds) throws SQLException {
    delegate.setQueryTimeout(seconds);
  }

  @Override
  public void cancel() throws SQLException {
    delegate.cancel();
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return delegate.getWarnings();
  }

  @Override
  public void clearWarnings() throws SQLException {
    delegate.clearWarnings();
  }

  @Override
  public void setCursorName(String name) throws SQLException {
    delegate.setCursorName(name);
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    return delegate.getResultSet();
  }

  @Override
  public int getUpdateCount() throws SQLException {
    return delegate.getUpdateCount();
  }

  @Override
  public boolean getMoreResults() throws SQLException {
    return delegate.getMoreResults();
  }

  @Override
  public void setFetchDirection(int direction) throws SQLException {
    delegate.setFetchDirection(direction);
  }

  @Override
  public int getFetchDirection() throws SQLException {
    return delegate.getFetchDirection();
  }

  @Override
  public void setFetchSize(int rows) throws SQLException {
    delegate.setFetchSize(rows);
  }

  @Override
  public int getFetchSize() throws SQLException {
    return delegate.getFetchSize();
  }

  @Override
  public int getResultSetConcurrency() throws SQLException {
    return delegate.getResultSetConcurrency();
  }

  @Override
  public int getResultSetType() throws SQLException {
    return delegate.getResultSetType();
  }

  @Override
  public boolean getMoreResults(int current) throws SQLException {
    return delegate.getMoreResults(current);
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    return delegate.getGeneratedKeys();
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    return delegate.getResultSetHoldability();
  }

  @Override
  public boolean isClosed() throws SQLException {
    return delegate.isClosed();
  }

  @Override
  public void setPoolable(boolean poolable) throws SQLException {
    delegate.setPoolable(poolable);
  }

  @Override
  public boolean isPoolable() throws SQLException {
    return delegate.isPoolable();
  }

  @Override
  public void closeOnCompletion() throws SQLException {
    delegate.closeOnCompletion();
  }

  @Override
  public boolean isCloseOnCompletion() throws SQLException {
    return delegate.isCloseOnCompletion();
  }

  @Override
  public long getLargeUpdateCount() throws SQLException {
    return delegate.getLargeUpdateCount();
  }

  @Override
  public void setLargeMaxRows(long max) throws SQLException {
    delegate.setLargeMaxRows(max);
  }

  @Override
  public long getLargeMaxRows() throws SQLException {
    return delegate.getLargeMaxRows();
  }

  public void newTxIdIfAutoCommit() {
    if (getIfAutoCommit()) {
      getEslConnection().newTxId();
    }
  }

  protected String getInitialExecutionStateByIfAutoCommit() {
    return getIfAutoCommit() ? Execution.EXECUTION_STATE_COMMITTED : Execution.EXECUTION_STATE_NEW;
  }

  protected boolean getIfAutoCommit() {
    return getEslConnection().isIfAutoCommit();
  }

  protected String getTxId() {
    return getEslConnection().getTxId();
  }

  // -----------------
  //   获取Connection
  // -----------------

  public EslConnection getEslConnection() {
    return eslConnection;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return getEslConnection().getConnection();
  }

  // -----------
  //   EslIded
  // -----------

  @Override
  public String getEslId() {
    return eslId;
  }

  protected long curMills() {
    return System.currentTimeMillis();
  }

  /**
   * batch update
   */
  protected void executeBatchUpdate(long start) {
    String executionState = getInitialExecutionStateByIfAutoCommit();
    eslStatementSqlAttachment.executeBatch(executionState, getTxId(), start, curMills());
    newTxIdIfAutoCommit();
  }

  protected void execute(String sql, long start) {
    if (SqlUtils.ifQuerySql(sql)) {
      executeQuery(sql, start);
    } else {
      executeUpdate(sql, start);
    }
  }

  protected void executeUpdate(String sql, long start) {
    String executionState = getInitialExecutionStateByIfAutoCommit();
    eslStatementSqlAttachment.executeSql(executionState, sql, getTxId(), start, curMills());
    newTxIdIfAutoCommit();
  }

  protected void executeQuery(String sql, long start) {
    eslStatementSqlAttachment.executeSql(Execution.EXECUTION_STATE_QUERY, sql, getTxId(), start, curMills());
  }

  protected String getDataSourceEslId() {
    if (eslConnection instanceof EslDataSourceConnection) {
      return ((EslDataSourceConnection) eslConnection).getEslDataSource().getEslId();
    }
    return null;
  }

  protected String getConnectionEslId() {
    return eslConnection.getEslId();
  }

  protected ExecutionQueue getExecutionQueue() {
    return eslConnection.getExecutionQueue();
  }

}
