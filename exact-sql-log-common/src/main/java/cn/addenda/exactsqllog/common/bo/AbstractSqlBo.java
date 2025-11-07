package cn.addenda.exactsqllog.common.bo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractSqlBo implements Execution {

  private String executionState;
  private String dataSourceEslId;
  private String connectionEslId;
  private String statementEslId;

  private String txId;

  private Long start;
  private Long end;

  @Override
  public String getExecutionState() {
    return executionState;
  }

  @Override
  public void setExecutionState(String executionState) {
    this.executionState = executionState;
  }

  @Override
  public String getDataSourceEslId() {
    return dataSourceEslId;
  }

  @Override
  public void setDataSourceEslId(String dataSourceEslId) {
    this.dataSourceEslId = dataSourceEslId;
  }

  @Override
  public String getConnectionEslId() {
    return connectionEslId;
  }

  @Override
  public void setConnectionEslId(String connectionEslId) {
    this.connectionEslId = connectionEslId;
  }

  @Override
  public String getStatementEslId() {
    return statementEslId;
  }

  @Override
  public void setStatementEslId(String statementEslId) {
    this.statementEslId = statementEslId;
  }

}
