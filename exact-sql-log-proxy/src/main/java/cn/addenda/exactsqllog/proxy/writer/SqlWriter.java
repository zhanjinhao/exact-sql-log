package cn.addenda.exactsqllog.proxy.writer;

import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.common.config.EslConnectionConfig;

public interface SqlWriter {

  void logCommit(Execution execution);

  void logRollback(Execution execution);

  void logQuery(Execution execution);

  void logEslConnectionConfig(EslConnectionConfig eslConnectionConfig);

}
