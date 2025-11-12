package cn.addenda.exactsqllog.agent.transform.hikari;

import cn.addenda.exactsqllog.agent.transform.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.InterceptorPointDefine;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class HikariConcurrentBagInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public String getEnhancedClassName() {
    return "com.zaxxer.hikari.util.ConcurrentBag";
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new HikariConcurrentBagInterceptorPoint());
  }

}
