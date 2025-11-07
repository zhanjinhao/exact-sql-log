package cn.addenda.exactsqllog.proxy.jdbc;

import cn.addenda.exactsqllog.common.util.UuidUtils;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.system.SystemLoggerFactory;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;
import lombok.Getter;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author addenda
 * @since 2024/8/24 17:03
 */
public class EslDataSource extends WrapperAdapter implements DataSource, EslIded, AutoCloseable {

  @Getter
  private final DataSource dataSource;

  private final SystemLoggerFactory systemLoggerFactory;

  private final SystemLogger systemLogger;

  private final SqlWriter sqlWriter;

  private final String eslId;

  private final SystemLogger eslDataSourceConnectionSystemLogger;

  public EslDataSource(DataSource dataSource, SystemLoggerFactory systemLoggerFactory, SqlWriter sqlWriter) {
    this.dataSource = dataSource;
    this.systemLoggerFactory = systemLoggerFactory;
    this.systemLogger = systemLoggerFactory.getSystemLogger(EslDataSource.class);
    this.sqlWriter = sqlWriter;
    this.eslId = UuidUtils.generateUuid();

    this.eslDataSourceConnectionSystemLogger = systemLoggerFactory.getSystemLogger(EslDataSourceConnection.class);
  }

  @Override
  public Connection getConnection() throws SQLException {
    Connection connection = dataSource.getConnection();
    EslConnection eslConnection = new EslDataSourceConnection(
            connection, this, eslDataSourceConnectionSystemLogger, sqlWriter);

    addEslConnection(eslConnection);

    return eslConnection;
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    Connection connection = dataSource.getConnection(username, password);
    EslConnection eslConnection = new EslDataSourceConnection(
            connection, this, eslDataSourceConnectionSystemLogger, sqlWriter);

    addEslConnection(eslConnection);

    return eslConnection;
  }

  protected PrintWriter logWriter = new PrintWriter(System.out);

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    return logWriter;
  }

  @Override
  public void setLogWriter(PrintWriter out) throws SQLException {
    this.logWriter = out;
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    dataSource.setLoginTimeout(seconds);
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return dataSource.getLoginTimeout();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {
    throw new SQLFeatureNotSupportedException();
  }

  // -----------
  //   EslIded
  // -----------

  public String getEslId() {
    return eslId;
  }

  @Override
  public void close() throws Exception {
    // 如果DataSource是AutoCloseable的实现，调用close()方法
    if (dataSource instanceof AutoCloseable) {
      ((AutoCloseable) dataSource).close();
    }
    closeEsl();
  }

  // ----------------------------
  //   EslConnection Management
  // ----------------------------

  @Override
  public synchronized void closeEsl() throws SQLException {
    // 按照先申请资源后释放的步骤，在DataSource关闭的时候，其创造的Connection一定都关闭完成了。
    // 但是，为了在遇到异常步骤时尽可能减少内存泄漏，在close这里还是释放一下
    closeAllEslConnection();
  }

  private final Map<String, EslConnection> eslConnectionMap = new HashMap<>();

  private synchronized void addEslConnection(EslConnection eslConnection) {
    eslConnectionMap.put(eslConnection.getEslId(), eslConnection);
  }

  public synchronized void removeEslConnection(EslConnection eslConnection) {
    eslConnectionMap.remove(eslConnection.getEslId());
  }

  private synchronized void closeAllEslConnection() throws SQLException {
    for (EslConnection eslConnection : eslConnectionMap.values()) {
      try {
        // close()方法执行的时候，会执行closeEsl()方法
        if (!eslConnection.isClosed()) {
          eslConnection.closeEsl();
        }
      } catch (SQLException e) {
        systemLogger.error("exception occurred when EslConnection close, {}.", eslConnection, e);
        throw e;
      }
    }
  }

  // -----------
  //   基础方法
  // -----------

  @Override
  public String toString() {
    return "EslDataSource{" +
            "dataSource=" + dataSource +
            ", eslId='" + eslId + '\'' +
            "} " + super.toString();
  }

}
