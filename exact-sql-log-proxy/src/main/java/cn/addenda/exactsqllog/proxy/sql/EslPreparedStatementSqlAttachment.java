package cn.addenda.exactsqllog.proxy.sql;

import cn.addenda.exactsqllog.common.bo.OrderedSql;
import cn.addenda.exactsqllog.common.bo.PreparedSqlBo;
import cn.addenda.exactsqllog.common.bo.PreparedStatementParameterWrapper;
import cn.addenda.exactsqllog.common.bo.SqlExecutionOrder;
import cn.addenda.exactsqllog.common.pojo.Binary;
import cn.addenda.exactsqllog.common.pojo.Ternary;
import cn.addenda.exactsqllog.common.pojo.Unary;
import cn.addenda.exactsqllog.proxy.jdbc.EslPreparedStatement;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 存储一个{@link EslPreparedStatement}里执行的SQL。 <br/>
 * 由于{@link PreparedStatement}具有{@link Statement}的功能，所以{@link EslPreparedStatementSqlAttachment}需要具有{@link EslStatementSqlAttachment}的功能。
 */
public class EslPreparedStatementSqlAttachment extends EslStatementSqlAttachment {

  private final String parameterizedSql;
  private final PreparedStatementParameterWrapper preparedStatementParameterWrapper;
  protected PreparedSqlBo stashedPreparedSqlBo;

  public EslPreparedStatementSqlAttachment(String parameterizedSql, ExecutionQueue executionQueue,
                                           String dataSourceEslId, String connectionEslId, String statementEslId) {
    super(executionQueue, dataSourceEslId, connectionEslId, statementEslId);
    this.parameterizedSql = parameterizedSql;
    this.preparedStatementParameterWrapper = new PreparedStatementParameterWrapper();
  }

  public void executePreparedSql(String executionState, String txId, long start, long end) {
    synchronized (executionQueue) {
      List<PreparedStatementParameterWrapper> preparedStatementParameterWrapperList = new ArrayList<>(1);
      preparedStatementParameterWrapperList.add(snapshotParameter());

      PreparedSqlBo preparedSqlBo = new PreparedSqlBo(executionState, dataSourceEslId, connectionEslId, statementEslId, parameterizedSql, preparedStatementParameterWrapperList, txId, start, end);
      executionQueue.output(preparedSqlBo);
    }
  }

  @Override
  public void executeBatch(String executionState, String txId, long start, long end) {
    synchronized (executionQueue) {
      if (stashedSqlBo == null) {
        if (stashedPreparedSqlBo == null) {
          return;
        } else {
          for (PreparedStatementParameterWrapper wrapper : stashedPreparedSqlBo.getPreparedStatementParameterWrapperList()) {
            wrapper.setOrder(getNextOrder());
          }
          output(executionState, txId, start, end);
        }
      } else {
        if (stashedPreparedSqlBo == null) {
          super.executeBatch(executionState, txId, start, end);
        } else {
          setOrder();
          super.executeBatch2(executionState, txId, start, end);
          output(executionState, txId, start, end);
        }
      }
    }
  }

  private void output(String executionState, String txId, long start, long end) {
    stashedPreparedSqlBo.setExecutionState(executionState);
    stashedPreparedSqlBo.setDataSourceEslId(dataSourceEslId);
    stashedPreparedSqlBo.setConnectionEslId(connectionEslId);
    stashedPreparedSqlBo.setStatementEslId(statementEslId);
    stashedPreparedSqlBo.setTxId(txId);
    stashedPreparedSqlBo.setStart(start);
    stashedPreparedSqlBo.setEnd(end);
    executionQueue.output(stashedPreparedSqlBo);
    stashedPreparedSqlBo = null;
  }

  private void setOrder() {
    // stashSqlBo和stashedPreparedSqlBo必须按照addBatch的顺序写出去
    List<PreparedStatementParameterWrapper> wrapperList = stashedPreparedSqlBo.getPreparedStatementParameterWrapperList();
    List<OrderedSql> orderedSqlList = stashedSqlBo.getOrderedSqlList();
    List<SqlExecutionOrder> sqlExecutionOrderList = new ArrayList<>();
    sqlExecutionOrderList.addAll(wrapperList);
    sqlExecutionOrderList.addAll(orderedSqlList);
    sqlExecutionOrderList.sort(Comparator.comparing(SqlExecutionOrder::getOrder));
    for (SqlExecutionOrder sqlExecutionOrder : sqlExecutionOrderList) {
      sqlExecutionOrder.setOrder(getNextOrder());
    }
  }

  @Override
  public void clearBatch() {
    synchronized (executionQueue) {
      super.clearBatch();
      if (stashedPreparedSqlBo != null) {
        stashedPreparedSqlBo.clear();
        stashedPreparedSqlBo = null;
      }
    }
  }

  public void addBatchPreparedSql() {
    synchronized (executionQueue) {
      if (stashedPreparedSqlBo == null) {
        stashedPreparedSqlBo = new PreparedSqlBo(parameterizedSql);
      }
      stashedPreparedSqlBo.getPreparedStatementParameterWrapperList().add(snapshotParameter());
    }
  }

  /**
   * 有序性指的是：按照execute时的顺序
   */
  private PreparedStatementParameterWrapper snapshotParameter() {
    preparedStatementParameterWrapper.setOrder(getNextBatchTmpOrder());
    return preparedStatementParameterWrapper.deepClone();
  }

  public void set(int parameterIndex, String setMethod, Unary<?> unary) {
    synchronized (executionQueue) {
      preparedStatementParameterWrapper.set(parameterIndex - 1, setMethod, unary);
    }
  }

  public void set(int parameterIndex, String setMethod, Binary<?, ?> binary) {
    synchronized (executionQueue) {
      preparedStatementParameterWrapper.set(parameterIndex - 1, setMethod, binary);
    }
  }

  public void set(int parameterIndex, String setMethod, Ternary<?, ?, ?> ternary) {
    synchronized (executionQueue) {
      preparedStatementParameterWrapper.set(parameterIndex - 1, setMethod, ternary);
    }
  }

  public void clearPreparedStatementParameterWrapper() {
    synchronized (executionQueue) {
      preparedStatementParameterWrapper.clear();
    }
  }

}
