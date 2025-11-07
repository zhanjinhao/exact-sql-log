package cn.addenda.exactsqllog.common.bo;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 不是{@link Statement}或{@link PreparedStatement}的execute顺序。是执行的SQL的顺序。
 */
public interface SqlExecutionOrder {

  int getOrder();

  void setOrder(int order);

}
