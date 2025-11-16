package cn.addenda.exactsqllog.agent.transform.hikari;

import cn.addenda.exactsqllog.agent.transform.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.InterceptorPointDefine;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;
import java.util.Set;

public class HikariConcurrentBagInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public Set<String> getEnhancedClassNameSet() {
    return ArrayUtils.asHashSet("com.zaxxer.hikari.util.ConcurrentBag");
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new HikariConcurrentBagInterceptorPoint());
  }

}
