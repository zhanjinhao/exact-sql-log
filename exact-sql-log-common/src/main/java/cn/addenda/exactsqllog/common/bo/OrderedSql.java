package cn.addenda.exactsqllog.common.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class OrderedSql implements SqlExecutionOrder {

  @Getter
  @Setter
  private String sql;

  private int order;

  public OrderedSql() {
  }

  public static OrderedSql of(String sql, int order) {
    OrderedSql orderedSql = new OrderedSql();
    orderedSql.setSql(sql);
    orderedSql.setOrder(order);
    return orderedSql;
  }

  public static OrderedSql of(String sql) {
    OrderedSql orderedSql = new OrderedSql();
    orderedSql.setSql(sql);
    return orderedSql;
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
