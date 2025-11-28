package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.eslstatement;

import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPointDefine;
import cn.addenda.exactsqllog.agent.transform.match.IndirectMatch;
import cn.addenda.exactsqllog.agent.transform.match.MultiClassNameMatch;
import cn.addenda.exactsqllog.common.util.ArrayUtils;

import java.util.List;

public class EslStatementInterceptorPointDefine implements InterceptorPointDefine {

  @Override
  public IndirectMatch getEnhancedClass() {
    return MultiClassNameMatch.of(
            "cn.addenda.exactsqllog.proxy.jdbc.EslStatement",
            "cn.addenda.exactsqllog.proxy.jdbc.EslPreparedStatement");
  }

  @Override
  public List<InterceptorPoint> getInterceptorPointList() {
    return ArrayUtils.asArrayList(new EslStatementInterceptorPoint());
  }

}
