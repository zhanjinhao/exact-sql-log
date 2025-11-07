package cn.addenda.exactsqllog.proxy.jdbc;

import java.sql.SQLException;

public interface EslIded {

  String getEslId();

  void closeEsl() throws SQLException;

}
