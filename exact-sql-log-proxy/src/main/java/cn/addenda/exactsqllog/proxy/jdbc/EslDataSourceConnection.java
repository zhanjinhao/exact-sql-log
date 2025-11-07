package cn.addenda.exactsqllog.proxy.jdbc;

import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author addenda
 * @since 2024/8/24 17:03
 */
public class EslDataSourceConnection extends EslConnection {

  @Getter
  private final EslDataSource eslDataSource;

  public EslDataSourceConnection(Connection connection, EslDataSource eslDataSource,
                                 SystemLogger systemLogger, SqlWriter sqlWriter) throws SQLException {
    super(connection, systemLogger, sqlWriter);
    this.eslDataSource = eslDataSource;
  }

  // ------------------------------------------------
  //   EslPreparedStatement&EslStatement Management
  // ------------------------------------------------

  @Override
  public synchronized void closeEsl() throws SQLException {
    super.closeEsl();
    // 从DataSource的ConnectionManager中移除当前Connection
    eslDataSource.removeEslConnection(this);
  }

  // -----------
  //   基础方法
  // -----------

  @Override
  public String toString() {
    return "EslDataSourceConnection{" +
            "eslDataSource=" + eslDataSource +
            "} " + super.toString();
  }

}
