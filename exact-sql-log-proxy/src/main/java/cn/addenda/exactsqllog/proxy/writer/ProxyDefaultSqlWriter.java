package cn.addenda.exactsqllog.proxy.writer;

import cn.addenda.exactsqllog.common.bo.Execution;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProxyDefaultSqlWriter implements SqlWriter {

  @Override
  public void logCommit(Execution execution) {
    log.info("commit: {}", execution);
  }

  @Override
  public void logRollback(Execution execution) {
    log.info("rollback: {}", execution);
  }

  @Override
  public void logQuery(Execution execution) {
    log.info("query: {}", execution);
  }

}
