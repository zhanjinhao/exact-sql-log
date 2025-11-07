package cn.addenda.exactsqllog.agent.writer;

import cn.addenda.exactsqllog.agent.ext.ExtFacade;
import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;

public class AgentHttpSqlWriter implements SqlWriter {

  @Override
  public void logCommit(Execution execution) {
    ExtFacade.sendExecution(execution);
  }

  @Override
  public void logRollback(Execution execution) {
    ExtFacade.sendExecution(execution);
  }

  @Override
  public void logQuery(Execution execution) {
    ExtFacade.sendExecution(execution);
  }

}
