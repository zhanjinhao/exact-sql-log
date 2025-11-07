package cn.addenda.exactsqllog.common.bo;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class PreparedStatementParameterWrapper implements SqlExecutionOrder {

  private int capacity = 0;

  private List<String> setMethodList = new ArrayList<>();

  private List<Object> parameterList = new ArrayList<>();

  private int order;

  public PreparedStatementParameterWrapper() {
  }

  public void set(int index, String setMethod, Object parameter) {
    if (index >= capacity) {
      for (int i = capacity; i <= index; i++) {
        setMethodList.add(null);
        parameterList.add(null);
      }
      capacity = index + 1;
    }
    setMethodList.set(index, setMethod);
    parameterList.set(index, parameter);
  }

  public void clear() {
    capacity = 0;
    setMethodList = new ArrayList<>();
    parameterList = new ArrayList<>();
  }

  public PreparedStatementParameterWrapper deepClone() {
    PreparedStatementParameterWrapper preparedStatementParameterWrapper = new PreparedStatementParameterWrapper();
    preparedStatementParameterWrapper.order = order;
    preparedStatementParameterWrapper.capacity = capacity;
    preparedStatementParameterWrapper.setMethodList = new ArrayList<>(setMethodList);
    // todo parameterList需不需要再deepClone
    preparedStatementParameterWrapper.parameterList = new ArrayList<>(parameterList);

    return preparedStatementParameterWrapper;
  }

  @Override
  public int getOrder() {
    return order;
  }

  @Override
  public void setOrder(int order) {
    this.order = order;
  }

}
