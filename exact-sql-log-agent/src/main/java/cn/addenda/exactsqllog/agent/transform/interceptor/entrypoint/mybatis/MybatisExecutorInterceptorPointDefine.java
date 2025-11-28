package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.mybatis;

import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.match.IndirectMatch;
import cn.addenda.exactsqllog.agent.transform.match.MultiClassNameMatch;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class MybatisExecutorInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public IndirectMatch getEnhancedClass() {
    return MultiClassNameMatch.of(
            "org.apache.ibatis.executor.BaseExecutor",
            "org.apache.ibatis.executor.CachingExecutor");
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new MybatisExecutorInterceptorPoint());
  }

}
