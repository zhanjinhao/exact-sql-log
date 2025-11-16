package cn.addenda.exactsqllog.proxy.sql;

import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 存一个connection里的所有execution
 */
public class ExecutionQueue {

  private final List<Execution> executionList = new ArrayList<>();

  private final SqlWriter sqlWriter;

  public ExecutionQueue(SqlWriter sqlWriter) {
    this.sqlWriter = sqlWriter;
  }

  public void propagateCommitted() {
    synchronized (this) {
      Iterator<Execution> iterator = executionList.iterator();
      while (iterator.hasNext()) {
        Execution execution = iterator.next();
        execution.setExecutionState(Execution.EXECUTION_STATE_COMMITTED);
        iterator.remove();
        sqlWriter.logCommit(execution);
      }
    }
  }

  public void propagateRollback() {
    synchronized (this) {
      Iterator<Execution> iterator = executionList.iterator();
      while (iterator.hasNext()) {
        Execution execution = iterator.next();
        execution.setExecutionState(Execution.EXECUTION_STATE_ROLLBACK);
        iterator.remove();
        sqlWriter.logRollback(execution);
      }
    }
  }

  private void outputQuery(Execution execution) {
    synchronized (this) {
      execution.setExecutionState(Execution.EXECUTION_STATE_QUERY);
      sqlWriter.logQuery(execution);
    }
  }

  public void output(Execution execution) {
    synchronized (this) {
      String executionState = execution.getExecutionState();
      if (Execution.EXECUTION_STATE_NEW.equals(executionState)) {
        executionList.add(execution);
      } else if (Execution.EXECUTION_STATE_COMMITTED.equals(executionState)) {
        executionList.add(execution);
        propagateCommitted();
      } else if (Execution.EXECUTION_STATE_QUERY.equals(executionState)) {
        outputQuery(execution);
      } else if (Execution.EXECUTION_STATE_ROLLBACK.equals(executionState)) {
        throw new UnsupportedOperationException(String.format("unsupported state: %s.", execution));
      }
    }
  }

}
