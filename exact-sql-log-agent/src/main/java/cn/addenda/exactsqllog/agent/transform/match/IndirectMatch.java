package cn.addenda.exactsqllog.agent.transform.match;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 所有非NameMatch的情况都要实现IndirectMatch
 */
public interface IndirectMatch extends ClassMatch {

  /**
   * 用于构造type()的参数。
   */
  ElementMatcher.Junction<? super TypeDescription> buildJunction();

  /**
   * 用于判断typeDescription是否匹配
   */
  boolean isMatch(TypeDescription typeDescription);

}
