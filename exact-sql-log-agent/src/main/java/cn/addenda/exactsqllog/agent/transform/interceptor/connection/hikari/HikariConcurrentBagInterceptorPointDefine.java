package cn.addenda.exactsqllog.agent.transform.interceptor.connection.hikari;

import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.match.ClassMatch;
import cn.addenda.exactsqllog.agent.transform.match.NameMatch;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class HikariConcurrentBagInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public ClassMatch getEnhancedClass() {
    return NameMatch.of("com.zaxxer.hikari.util.ConcurrentBag");
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new HikariConcurrentBagInterceptorPoint());
  }

}
