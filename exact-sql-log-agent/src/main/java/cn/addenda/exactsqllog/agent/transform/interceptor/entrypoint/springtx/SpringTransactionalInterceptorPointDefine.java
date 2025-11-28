package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springtx;

import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.match.IndirectMatch;
import cn.addenda.exactsqllog.agent.transform.match.MethodAnnotationMatch;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class SpringTransactionalInterceptorPointDefine implements InterceptorPointDefine {

  private static final String TRANSACTIONAL_NAME = "org.springframework.transaction.annotation.Transactional";

  @Override
  public IndirectMatch getEnhancedClass() {
    return MethodAnnotationMatch.of(TRANSACTIONAL_NAME);
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new SpringTransactionalInterceptorPoint());
  }

}
