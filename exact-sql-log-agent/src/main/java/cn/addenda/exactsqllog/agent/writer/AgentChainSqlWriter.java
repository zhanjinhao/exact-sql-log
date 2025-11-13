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

  private static final SystemLogger systemLogger =
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
    private volatile LinkedBlockingQueue<Execution> executionQueue;
    private volatile Thread taskConsumer;

    public TaskConsumer(NamedBiConsumer<SqlWriter, Execution> biConsumer) {
      this.biConsumer = biConsumer;
      this.executionQueue = new LinkedBlockingQueue<>(10000);
      this.taskConsumer = new Thread(this::run);
      this.taskConsumer.setDaemon(true);
      this.taskConsumer.setName("AgentChainSqlWriter-taskConsumer-" + biConsumer.getName() + "-Thread");
      this.taskConsumer.start();
    }

    public boolean offer(Execution execution) {
      return executionQueue.offer(execution);
    }

    private void run() {
      while (true) {
        Execution take = null;
        try {
          take = executionQueue.take();
          doAccept(take);
        } catch (InterruptedException e) {
          systemLogger.debug("{}关闭", taskConsumer.getName());
          Thread.currentThread().interrupt();
          break;
        } catch (Throwable t) {
          systemLogger.error("unexpected error: [{}].", take, t);
        }
      }
    }

    @Override
    public Integer getOrder() {
      return 0;
    }

    @Override
    public void shutdown() {
      if (taskConsumer != null) {
        taskConsumer.interrupt();
      }
      if (executionQueue == null) {
        return;
      }
      Execution[] array = executionQueue.toArray(new Execution[]{});
      if (array.length > 0) {
        systemLogger.error("[{}]已关闭, 还有[{}]个Execution未被执行", biConsumer.getName(), array.length);
        for (Execution execution : array) {
          doAccept(execution);
        }
      }
    }

    private void doAccept(Execution take) {
      for (SqlWriter sqlWriter : sqlWriterList) {
        try {
          biConsumer.accept(sqlWriter, take);
        } catch (Throwable t) {
          systemLogger.error("[{}] {} error. Execution is [{}].",
                  sqlWriter, biConsumer.getName(), ExtFacade.toStr(take), t);
        }
      }
    }
  }

  private interface NamedBiConsumer<T, U> extends BiConsumer<T, U> {

    String getName();

  }

}
