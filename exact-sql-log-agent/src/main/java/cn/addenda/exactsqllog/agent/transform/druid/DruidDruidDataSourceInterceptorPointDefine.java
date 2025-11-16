package cn.addenda.exactsqllog.agent.transform.druid;

import cn.addenda.exactsqllog.agent.transform.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.InterceptorPointDefine;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;
import java.util.Set;

public class DruidDruidDataSourceInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public Set<String> getEnhancedClassNameSet() {
    return ArrayUtils.asHashSet("com.alibaba.druid.pool.DruidDataSource");
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new DruidDruidDataSourceInterceptorPoint());
  }

}
