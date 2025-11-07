package cn.addenda.exactsqllog.common.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * 1、一次{@link PreparedStatement#executeBatch()}或{@link PreparedStatement#executeLargeBatch()} 执行，preparedStatementParameterWrapperList的size()大于1。 <br/>
 * 2、一次{@link PreparedStatement}的其它execute执行，preparedStatementParameterWrapperList的size()等于1。
 */
@Setter
@Getter
@ToString(callSuper = true)
public class PreparedSqlBo extends AbstractSqlBo {

  private String parameterizedSql;

  private List<PreparedStatementParameterWrapper> preparedStatementParameterWrapperList;

  public PreparedSqlBo(String parameterizedSql) {
    super();
    this.parameterizedSql = parameterizedSql;
    this.preparedStatementParameterWrapperList = new ArrayList<>();
  }

  public PreparedSqlBo(String executionState, String dataSourceEslId, String connectionEslId, String statementEslId,
                       String parameterizedSql, List<PreparedStatementParameterWrapper> preparedStatementParameterWrapperList, String txId, long start, long end) {
    super(executionState, dataSourceEslId, connectionEslId, statementEslId, txId, start, end);
    this.parameterizedSql = parameterizedSql;
    this.preparedStatementParameterWrapperList = preparedStatementParameterWrapperList;
  }

  public void clear() {
    this.preparedStatementParameterWrapperList = new ArrayList<>();
  }

}
