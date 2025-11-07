package cn.addenda.exactsqllog.proxy.writer;

import cn.addenda.exactsqllog.common.bo.Execution;

public interface SqlWriter {

  void logCommit(Execution execution);

  void logRollback(Execution execution);

  void logQuery(Execution execution);

}
