package cn.addenda.exactsqllog.agent.transform.match;

import cn.addenda.exactsqllog.agent.ExactSqlLogAgentStartException;
import cn.addenda.exactsqllog.common.util.ArrayUtils;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

/**
 * 匹配多个类名相等的情况
 */
public class MultiClassNameMatch implements IndirectMatch {

  /**
   * 要匹配的类名称
   */
  private final List<String> classNameList;

  public MultiClassNameMatch(String[] classNames) {
    if (classNames == null || classNames.length == 0) {
      throw new ExactSqlLogAgentStartException("classNames should has 1 element at least");
    } else {
      this.classNameList = ArrayUtils.asArrayList(classNames);
    }
  }

  /**
   * 多个类要求是or的关系
   */
  @Override
  public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
    ElementMatcher.Junction<? super TypeDescription> junction = null;
    for (String className : classNameList) {
      if (junction == null) {
        junction = ElementMatchers.named(className);
      } else {
        junction = junction.or(ElementMatchers.named(className));
      }
    }
    return junction;
  }

  @Override
  public boolean isMatch(TypeDescription typeDescription) {
    return classNameList.contains(typeDescription.getTypeName());
  }

  public static MultiClassNameMatch of(String... classNames) {
    return new MultiClassNameMatch(classNames);
  }

}
