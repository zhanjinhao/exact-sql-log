package cn.addenda.exactsqllog.agent.transform.mysql;

import cn.addenda.exactsqllog.agent.transform.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.InterceptorPointDefine;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class MySQLDriverInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public String getEnhancedClassName() {
    return "com.mysql.cj.jdbc.NonRegisteringDriver";
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new MySQLDriverInterceptorPoint());
  }

}
