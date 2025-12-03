package cn.addenda.exactsqllog.agent.writer;

import cn.addenda.exactsqllog.agent.ext.ExtFacade;
import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.common.bo.PreparedSqlBo;
import cn.addenda.exactsqllog.common.bo.SqlBo;
import cn.addenda.exactsqllog.common.config.EslConnectionConfig;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;

public class AgentHttpSqlWriter implements SqlWriter {

  private static final SystemLogger log = AgentDefaultSystemLoggerFactory.getInstance()
          .getSystemLogger(AgentHttpSqlWriter.class);

  @Override
  public void logCommit(Execution execution) {
    if (execution instanceof SqlBo) {
      ExtFacade.sendSqlBo((SqlBo) execution);
    } else {
      ExtFacade.sendPreparedSqlBo((PreparedSqlBo) execution);
    }
  }

  @Override
  public void logRollback(Execution execution) {
    if (execution instanceof SqlBo) {
      ExtFacade.sendSqlBo((SqlBo) execution);
    } else {
      ExtFacade.sendPreparedSqlBo((PreparedSqlBo) execution);
    }
  }

  @Override
  public void logQuery(Execution execution) {
    if (execution instanceof SqlBo) {
      ExtFacade.sendSqlBo((SqlBo) execution);
    } else {
      ExtFacade.sendPreparedSqlBo((PreparedSqlBo) execution);
    }
  }

  @Override
  public void logEslConnectionConfig(EslConnectionConfig eslConnectionConfig) {
    // 如果失败，间隔3s后重试，直至100次之后不再重试
    ExtFacade.sendEslConnectionConfig(eslConnectionConfig);
  }

}
