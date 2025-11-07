package cn.addenda.exactsqllog.common.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 1、一次{@link Statement#executeBatch()}或{@link Statement#executeLargeBatch()} 执行，sqlList的size()大于1。 <br/>
 * 2、一次{@link Statement}的其它execute执行，sqlList的size()等于1。
 */
@Setter
@Getter
@ToString(callSuper = true)
public class SqlBo extends AbstractSqlBo {

  private List<OrderedSql> orderedSqlList;

  public SqlBo() {
    this.orderedSqlList = new ArrayList<>();
  }

  public SqlBo(String executionState, String dataSourceEslId, String connectionEslId, String statementEslId,
               List<OrderedSql> orderedSqlList, String txId, long start, long end) {
    super(executionState, dataSourceEslId, connectionEslId, statementEslId, txId, start, end);
    this.orderedSqlList = orderedSqlList;
  }

  public void clear() {
    this.orderedSqlList = new ArrayList<>();
  }

}
