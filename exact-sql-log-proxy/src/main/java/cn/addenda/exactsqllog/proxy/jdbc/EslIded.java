package cn.addenda.exactsqllog.proxy.jdbc;

import java.sql.SQLException;

public interface EslIded {

  String getEslId();

  /**
   * 这个方法必须保证幂等性
   */
  void closeEsl() throws SQLException;

}
