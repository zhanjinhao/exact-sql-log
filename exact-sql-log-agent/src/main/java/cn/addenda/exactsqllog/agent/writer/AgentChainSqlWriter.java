package cn.addenda.exactsqllog.agent.writer;

import cn.addenda.exactsqllog.agent.AgentPackage;
import cn.addenda.exactsqllog.agent.ext.ExtFacade;
import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.common.bo.Execution;
import cn.addenda.exactsqllog.common.config.EslConnectionConfig;
import cn.addenda.exactsqllog.common.jvm.JVMShutdown;
import cn.addenda.exactsqllog.common.jvm.JVMShutdownCallback;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import cn.addenda.exactsqllog.proxy.writer.SqlWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;

public class AgentChainSqlWriter implements SqlWriter {

  private static final SystemLogger log =
          AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(AgentChainSqlWriter.class);

  private final List<SqlWriter> sqlWriterList;

  private final TaskConsumer<Execution> logCommitTaskConsumer =
          new TaskConsumer<>(new NamedBiConsumer<SqlWriter, Execution>() {
            @Override
            public String getName() {
              return "logCommit";
            }

            @Override
            public void accept(SqlWriter sqlWriter, Execution execution) {
              sqlWriter.logCommit(execution);
            }
          }, 10000);

  private final TaskConsumer<Execution> logRollbackTaskConsumer =
          new TaskConsumer<>(new NamedBiConsumer<SqlWriter, Execution>() {
            @Override
            public String getName() {
              return "logRollback";
            }

            @Override
            public void accept(SqlWriter sqlWriter, Execution execution) {
              sqlWriter.logRollback(execution);
            }
          }, 10000);

  private final TaskConsumer<Execution> logQueryTaskConsumer =
          new TaskConsumer<>(new NamedBiConsumer<SqlWriter, Execution>() {
            @Override
            public String getName() {
              return "logQuery";
            }

            @Override
            public void accept(SqlWriter sqlWriter, Execution execution) {
              sqlWriter.logQuery(execution);
            }
          }, 10000);

  private final TaskConsumer<EslConnectionConfig> logEslConnectionConfigTaskConsumer =
          new TaskConsumer<>(new NamedBiConsumer<SqlWriter, EslConnectionConfig>() {
            @Override
            public String getName() {
              return "logEslConnectionConfig";
            }

            @Override
            public void accept(SqlWriter sqlWriter, EslConnectionConfig eslConnectionConfig) {
              sqlWriter.logEslConnectionConfig(eslConnectionConfig);
            }
          }, 1000);

  public AgentChainSqlWriter() {
    sqlWriterList = initSqlWriter();
    initJvmShutdown();
  }

  private List<SqlWriter> initSqlWriter() {
    List<SqlWriter> _sqlWriterList = new ArrayList<>();
    Properties agentProperties = AgentPackage.getAgentProperties();
    String sqlWriterImplClass = agentProperties.getProperty("sqlWriter.impl");
    if (sqlWriterImplClass == null || sqlWriterImplClass.isEmpty()) {
      return _sqlWriterList;
    }

    String[] sqlWriterImplClassnames = sqlWriterImplClass.split(",");
    for (String sqlWriterImplClassname : sqlWriterImplClassnames) {
      Optional.ofNullable(init(sqlWriterImplClassname)).ifPresent(_sqlWriterList::add);
    }
    return _sqlWriterList;
  }

  private SqlWriter init(String className) {
    try {
      Class<?> clazz = Class.forName(className);
      return (SqlWriter) clazz.newInstance();
    } catch (Exception e) {
      log.error("初始化SqlWriter[{}]失败。", className, e);
      return null;
    }
  }

  private void initJvmShutdown() {
    JVMShutdown.getInstance().addJvmShutdownCallback(logCommitTaskConsumer);
    JVMShutdown.getInstance().addJvmShutdownCallback(logRollbackTaskConsumer);
    JVMShutdown.getInstance().addJvmShutdownCallback(logQueryTaskConsumer);
    JVMShutdown.getInstance().addJvmShutdownCallback(logEslConnectionConfigTaskConsumer);
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

  @Override
  public void logEslConnectionConfig(EslConnectionConfig eslConnectionConfig) {
    logEslConnectionConfigTaskConsumer.offer(eslConnectionConfig);
  }

  private class TaskConsumer<T> implements JVMShutdownCallback {
    private final NamedBiConsumer<SqlWriter, T> biConsumer;
    private final LinkedBlockingQueue<T> taskQueue;
    private final Thread taskConsumerThread;
    private volatile boolean ifRunning = false;

    public TaskConsumer(NamedBiConsumer<SqlWriter, T> biConsumer, int queueSize) {
      this.biConsumer = biConsumer;
      this.taskQueue = new LinkedBlockingQueue<>(queueSize);
      this.taskConsumerThread = new Thread(this::run);
      this.taskConsumerThread.setDaemon(true);
      this.taskConsumerThread.setName("AgentChainSqlWriter-taskConsumer-" + biConsumer.getName() + "-Thread");
      this.taskConsumerThread.start();
      this.ifRunning = true;
    }

    public void offer(T task) {
      if (!ifRunning) {
        log.error("TaskConsumer[{}]未在运行，无法处理Task[{}]。", ExtFacade.toStr(task));
        return;
      }
      boolean offer = taskQueue.offer(task);
      if (!offer) {
        log.error("TaskConsumer[{}]的队列已满，无法处理Task[{}]。", ExtFacade.toStr(task));
        return;
      }
    }

    private void run() {
      while (true) {
        T take = null;
        try {
          take = taskQueue.take();
          doAccept(take);
        } catch (InterruptedException e) {
          log.debug("{}关闭", taskConsumerThread.getName());
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
      if (taskConsumerThread != null) {
        taskConsumerThread.interrupt();
      }
      if (taskQueue == null) {
        return;
      }
      Object[] array = taskQueue.toArray();
      if (array.length > 0) {
        log.error("[{}]已关闭, 还有[{}]个task未被执行。", biConsumer.getName(), array.length);
        for (Object task : array) {
          doAccept((T) task);
        }
      } else {
        log.info("[{}]已关闭, 所有task都执行完成。", biConsumer.getName(), array.length);
      }
    }

    private void doAccept(T task) {
      for (SqlWriter sqlWriter : sqlWriterList) {
        try {
          biConsumer.accept(sqlWriter, task);
        } catch (Throwable t) {
          log.error("[{}] {} error. Task is [{}].",
                  sqlWriter, biConsumer.getName(), ExtFacade.toStr(task), t);
        }
      }
    }
  }

  private interface NamedBiConsumer<T, U> extends BiConsumer<T, U> {

    String getName();

  }

}
