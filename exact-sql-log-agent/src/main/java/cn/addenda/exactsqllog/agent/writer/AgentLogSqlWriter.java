package cn.addenda.exactsqllog.agent.writer;

import cn.addenda.exactsqllog.agent.ext.ExtFacade;
import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.common.config.EslConnectionConfig;
import cn.addenda.exactsqllog.ext.facade.LogFacade;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;

public class AgentLogSqlWriter implements SqlWriter {

  private final LogFacade logFacade;

  public AgentLogSqlWriter() {
    logFacade = ExtFacade.createLogFacade(AgentLogSqlWriter.class, AgentLogSqlWriter.class.getName());
  }

  @Override
  public void logCommit(Execution execution) {
    logFacade.info("commit: " + ExtFacade.toStr(execution));
  }

  @Override
  public void logRollback(Execution execution) {
    logFacade.info("rollback: " + ExtFacade.toStr(execution));
  }

  @Override
  public void logQuery(Execution execution) {
    logFacade.info("query: " + ExtFacade.toStr(execution));
  }

  @Override
  public void logEslConnectionConfig(EslConnectionConfig eslConnectionConfig) {
    logFacade.info("eslConnectionConfig: " + ExtFacade.toStr(eslConnectionConfig));
  }

}
