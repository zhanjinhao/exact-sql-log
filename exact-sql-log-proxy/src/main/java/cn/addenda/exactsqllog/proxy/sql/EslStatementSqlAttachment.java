package cn.addenda.exactsqllog.proxy.sql;

import cn.addenda.exactsqllog.common.bo.OrderedSql;
import cn.addenda.exactsqllog.common.bo.SqlBo;
import cn.addenda.exactsqllog.proxy.jdbc.EslStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 存储一个{@link EslStatement}里执行的SQL。
 */
public class EslStatementSqlAttachment {

  protected SqlBo stashedSqlBo;
  private final AtomicInteger orderGenerator = new AtomicInteger(0);
  private final AtomicInteger batchTmpOrderGenerator = new AtomicInteger(0);
  protected final ExecutionQueue executionQueue;
  protected final String dataSourceEslId;
  protected final String connectionEslId;
  protected final String statementEslId;

  public EslStatementSqlAttachment(ExecutionQueue executionQueue,
                                   String dataSourceEslId, String connectionEslId, String statementEslId) {
    this.executionQueue = executionQueue;
    this.dataSourceEslId = dataSourceEslId;
    this.connectionEslId = connectionEslId;
    this.statementEslId = statementEslId;
  }

  public void executeSql(String executionState,
                         String sql, String txId, long start, long end) {
    synchronized (executionQueue) {
      List<OrderedSql> orderedSqlList = new ArrayList<>(1);
      orderedSqlList.add(OrderedSql.of(sql, getNextOrder()));
      SqlBo sqlBo = new SqlBo(executionState, dataSourceEslId, connectionEslId, statementEslId, orderedSqlList, txId, start, end);
      executionQueue.output(sqlBo);
    }
  }

  public void executeBatch(String executionState,
                           String txId, long start, long end) {
    synchronized (executionQueue) {
      if (stashedSqlBo != null) {
        for (OrderedSql orderedSql : stashedSqlBo.getOrderedSqlList()) {
          orderedSql.setOrder(getNextOrder());
        }
        executeBatch2(executionState, txId, start, end);
      }
    }
  }

  protected void executeBatch2(String executionState,
                               String txId, long start, long end) {
    synchronized (executionQueue) {
      stashedSqlBo.setExecutionState(executionState);
      stashedSqlBo.setDataSourceEslId(dataSourceEslId);
      stashedSqlBo.setConnectionEslId(connectionEslId);
      stashedSqlBo.setStatementEslId(statementEslId);
      stashedSqlBo.setTxId(txId);
      stashedSqlBo.setStart(start);
      stashedSqlBo.setEnd(end);
      executionQueue.output(stashedSqlBo);
      stashedSqlBo = null;
    }
  }

  public void clearBatch() {
    synchronized (executionQueue) {
      if (stashedSqlBo != null) {
        stashedSqlBo.clear();
        stashedSqlBo = null;
      }
    }
  }

  public void addBatchSql(String sql) {
    synchronized (executionQueue) {
      if (stashedSqlBo == null) {
        stashedSqlBo = new SqlBo();
      }
      stashedSqlBo.getOrderedSqlList().add(OrderedSql.of(sql, getNextBatchTmpOrder()));
    }
  }

  protected int getNextOrder() {
    return orderGenerator.getAndIncrement();
  }

  protected int getNextBatchTmpOrder() {
    return batchTmpOrderGenerator.getAndIncrement();
  }

}
