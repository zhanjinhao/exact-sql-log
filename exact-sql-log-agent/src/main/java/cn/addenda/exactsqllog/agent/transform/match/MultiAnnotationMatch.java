package cn.addenda.exactsqllog.agent.transform.match;

import cn.addenda.exactsqllog.agent.ExactSqlLogAgentBootstrapException;
import cn.addenda.exactsqllog.common.util.ArrayUtils;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

/**
 * 匹配多个类名相等的情况
 */
public class MultiAnnotationMatch implements IndirectMatch {

  /**
   * 要匹配的类名称
   */
  private final List<String> annotationList;

  public MultiAnnotationMatch(String[] annotations) {
    if (annotations == null || annotations.length == 0) {
      throw new ExactSqlLogAgentBootstrapException("annotations should has 1 element at least");
    } else {
      this.annotationList = ArrayUtils.asArrayList(annotations);
    }
  }

  /**
   * 多个注解要求是or的关系
   */
  @Override
  public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
    ElementMatcher.Junction<? super TypeDescription> junction = null;
    for (String annotation : annotationList) {
      if (junction == null) {
        junction = ElementMatchers.isAnnotatedWith(ElementMatchers.named(annotation));
      } else {
        junction = junction.or(ElementMatchers.isAnnotatedWith(ElementMatchers.named(annotation)));
      }
    }
    return junction;
  }

  @Override
  public boolean isMatch(TypeDescription typeDescription) {
    AnnotationList declaredAnnotations = typeDescription.getDeclaredAnnotations();
    for (AnnotationDescription annotationDescription : declaredAnnotations) {
      for (String annotation : annotationList) {
        if (annotationDescription.getAnnotationType().getActualName().equals(annotation)) {
          return true;
        }
      }
    }
    return false;
  }

  public static MultiAnnotationMatch of(String... classNames) {
    return new MultiAnnotationMatch(classNames);
  }

}
