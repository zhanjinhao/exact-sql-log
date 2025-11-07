package cn.addenda.exactsqllog.agent.writer;

import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgentChainSqlWriter implements SqlWriter {

  private final List<SqlWriter> sqlWriterList;

  public AgentChainSqlWriter(List<SqlWriter> sqlWriterList) {
    if (sqlWriterList != null) {
      this.sqlWriterList = sqlWriterList;
    } else {
      this.sqlWriterList = new ArrayList<>();
    }
  }

  public AgentChainSqlWriter(SqlWriter... sqlWriters) {
    sqlWriterList = new ArrayList<>();
    if (sqlWriters != null) {
      Collections.addAll(this.sqlWriterList, sqlWriters);
    }
  }

  @Override
  public void logCommit(Execution execution) {
    for (SqlWriter sqlWriter : sqlWriterList) {
      sqlWriter.logCommit(execution);
    }
  }

  @Override
  public void logRollback(Execution execution) {
    for (SqlWriter sqlWriter : sqlWriterList) {
      sqlWriter.logRollback(execution);
    }
  }

  @Override
  public void logQuery(Execution execution) {
    for (SqlWriter sqlWriter : sqlWriterList) {
      sqlWriter.logQuery(execution);
    }
  }

}
