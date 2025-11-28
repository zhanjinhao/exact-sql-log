package cn.addenda.exactsqllog.agent.transform.interceptor.entrypoint.eslstatement;

import cn.addenda.exactsqllog.agent.transform.interceptor.Interceptor;
import cn.addenda.exactsqllog.agent.transform.interceptor.InterceptorPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class EslStatementInterceptorPoint implements InterceptorPoint {

  @Override
  public ElementMatcher<MethodDescription> getMethodsMatcher() {
    return ElementMatchers.nameStartsWith("execute")
            .and(ElementMatchers.isOverriddenFrom(Statement.class)
                    .or(ElementMatchers.isOverriddenFrom(PreparedStatement.class)))
            .and(ElementMatchers.isPublic());
  }

  @Override
  public Interceptor getInterceptor() {
    return new EslStatementExecuteInterceptor();
  }

}
