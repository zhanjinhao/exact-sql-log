package cn.addenda.exactsqllog.agent.transform.mysql;

import cn.addenda.exactsqllog.agent.transform.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.InterceptorPointDefine;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;
import java.util.Set;

public class MySQLDriverInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public Set<String> getEnhancedClassNameSet() {
    return ArrayUtils.asHashSet("com.mysql.cj.jdbc.NonRegisteringDriver");
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new MySQLDriverInterceptorPoint());
  }

}
