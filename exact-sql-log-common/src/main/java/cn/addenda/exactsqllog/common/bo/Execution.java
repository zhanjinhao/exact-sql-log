package cn.addenda.exactsqllog.common.bo;

import java.beans.Statement;
import java.sql.PreparedStatement;

/**
 * 一个实例代表一次{@link Statement}或{@link PreparedStatement}的execute
 */
public interface Execution {

  String EXECUTION_STATE_NEW = "NEW";
  String EXECUTION_STATE_QUERY = "QUERY";
  String EXECUTION_STATE_COMMITTED = "COMMITTED";
  String EXECUTION_STATE_ROLLBACK = "ROLLBACK";

  String getExecutionState();

  void setExecutionState(String executionState);

  String getDataSourceEslId();

  void setDataSourceEslId(String dataSourceEslId);

  String getConnectionEslId();

  void setConnectionEslId(String connectionEslId);

  String getStatementEslId();

  void setStatementEslId(String statementEslId);

}
