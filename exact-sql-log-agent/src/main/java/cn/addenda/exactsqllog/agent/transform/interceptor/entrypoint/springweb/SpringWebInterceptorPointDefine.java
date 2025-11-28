package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.springweb;

import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.match.IndirectMatch;
import cn.addenda.exactsqllog.agent.transform.match.MultiAnnotationMatch;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class SpringWebInterceptorPointDefine implements InterceptorPointDefine {

  private static final String CONTROLLER_NAME = "org.springframework.stereotype.Controller";
  private static final String REST_CONTROLLER_NAME = "org.springframework.web.bind.annotation.RestController";

  @Override
  public IndirectMatch getEnhancedClass() {
    return MultiAnnotationMatch.of(CONTROLLER_NAME, REST_CONTROLLER_NAME);
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new SpringWebInterceptorPoint());
  }

}
