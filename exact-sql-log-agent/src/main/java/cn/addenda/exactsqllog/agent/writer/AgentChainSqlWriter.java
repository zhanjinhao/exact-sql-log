package cn.addenda.exactsqllog.agent.writer;

import cn.addenda.exactsqllog.agent.ext.ExtFacade;
import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.common.jvm.JVMShutdown;
import cn.addenda.exactsqllog.common.jvm.JVMShutdownCallback;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;

public class AgentChainSqlWriter implements SqlWriter {

  private static final SystemLogger log =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(AgentChainSqlWriter.class);

  private final List<SqlWriter> sqlWriterList;

  private final TaskConsumer logCommitTaskConsumer =
          new TaskConsumer(new NamedBiConsumer<SqlWriter, Execution>() {
            @Override
            public String getName() {
              return "logCommit";
            }

            @Override
            public void accept(SqlWriter sqlWriter, Execution execution) {
              sqlWriter.logCommit(execution);
            }
          });

  private final TaskConsumer logRollbackTaskConsumer =
          new TaskConsumer(new NamedBiConsumer<SqlWriter, Execution>() {
            @Override
            public String getName() {
              return "logRollback";
            }

            @Override
            public void accept(SqlWriter sqlWriter, Execution execution) {
              sqlWriter.logRollback(execution);
            }
          });

  private final TaskConsumer logQueryTaskConsumer =
          new TaskConsumer(new NamedBiConsumer<SqlWriter, Execution>() {
            @Override
            public String getName() {
              return "logQuery";
            }

            @Override
            public void accept(SqlWriter sqlWriter, Execution execution) {
              sqlWriter.logQuery(execution);
            }
          });

  public AgentChainSqlWriter(List<SqlWriter> sqlWriterList) {
    if (sqlWriterList != null) {
      this.sqlWriterList = sqlWriterList;
    } else {
      this.sqlWriterList = new ArrayList<>();
    }
    initJvmShutdown();
  }

  public AgentChainSqlWriter(SqlWriter... sqlWriters) {
    sqlWriterList = new ArrayList<>();
    if (sqlWriters != null) {
      Collections.addAll(this.sqlWriterList, sqlWriters);
    }
    initJvmShutdown();
  }

  private void initJvmShutdown() {
    JVMShutdown.getInstance().addJvmShutdownCallback(logCommitTaskConsumer);
    JVMShutdown.getInstance().addJvmShutdownCallback(logRollbackTaskConsumer);
    JVMShutdown.getInstance().addJvmShutdownCallback(logQueryTaskConsumer);
  }

  @Override
  public void logCommit(Execution execution) {
    logCommitTaskConsumer.offer(execution);
  }

  @Override
  public void logRollback(Execution execution) {
    logRollbackTaskConsumer.offer(execution);
  }

  @Override
  public void logQuery(Execution execution) {
    logQueryTaskConsumer.offer(execution);
  }

  private class TaskConsumer implements JVMShutdownCallback {
    private final NamedBiConsumer<SqlWriter, Execution> biConsumer;
    private final LinkedBlockingQueue<Execution> executionQueue;
    private final Thread taskConsumer;
    private volatile boolean ifRunning = false;

    public TaskConsumer(NamedBiConsumer<SqlWriter, Execution> biConsumer) {
      this.biConsumer = biConsumer;
      this.executionQueue = new LinkedBlockingQueue<>(10000);
      this.taskConsumer = new Thread(this::run);
      this.taskConsumer.setDaemon(true);
      this.taskConsumer.setName("AgentChainSqlWriter-taskConsumer-" + biConsumer.getName() + "-Thread");
      this.taskConsumer.start();
      this.ifRunning = true;
    }

    public void offer(Execution execution) {
      if (!ifRunning) {
        log.error("TaskConsumer[{}]未在运行，无法处理Execution[{}]。", ExtFacade.toStr(execution));
        return;
      }
      boolean offer = executionQueue.offer(execution);
      if (!offer) {
        log.error("TaskConsumer[{}]的队列已满，无法处理Execution[{}]。", ExtFacade.toStr(execution));
        return;
      }
    }

    private void run() {
      while (true) {
        Execution take = null;
        try {
          take = executionQueue.take();
          doAccept(take);
        } catch (InterruptedException e) {
          log.debug("{}关闭", taskConsumer.getName());
          Thread.currentThread().interrupt();
          break;
        } catch (Throwable t) {
          log.error("unexpected error: [{}].", ExtFacade.toStr(take), t);
        }
      }
    }

    @Override
    public Integer getOrder() {
      return 0;
    }

    @Override
    public void shutdown() {
      this.ifRunning = false;
      if (taskConsumer != null) {
        taskConsumer.interrupt();
      }
      if (executionQueue == null) {
        return;
      }
      Execution[] array = executionQueue.toArray(new Execution[]{});
      if (array.length > 0) {
        log.error("[{}]已关闭, 还有[{}]个Execution未被执行。", biConsumer.getName(), array.length);
        for (Execution execution : array) {
          doAccept(execution);
        }
      } else {
        log.info("[{}]已关闭, 所有Execution都执行完成。", biConsumer.getName(), array.length);
      }
    }

    private void doAccept(Execution execution) {
      for (SqlWriter sqlWriter : sqlWriterList) {
        try {
          biConsumer.accept(sqlWriter, execution);
        } catch (Throwable t) {
          log.error("[{}] {} error. Execution is [{}].",
                  sqlWriter, biConsumer.getName(), ExtFacade.toStr(execution), t);
        }
      }
    }
  }

  private interface NamedBiConsumer<T, U> extends BiConsumer<T, U> {

    String getName();

  }

}
