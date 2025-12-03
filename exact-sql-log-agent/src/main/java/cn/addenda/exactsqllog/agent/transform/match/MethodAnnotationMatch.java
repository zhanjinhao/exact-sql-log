package cn.addenda.exactsqllog.agent.transform.match;

import cn.addenda.exactsqllog.agent.ExactSqlLogAgentStartException;
import cn.addenda.exactsqllog.common.util.ArrayUtils;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * Match the class, which has methods with the certain annotations. This is a very complex match.
 */
public class MethodAnnotationMatch implements IndirectMatch {

  private final List<String> annotationList;

  private MethodAnnotationMatch(String[] annotations) {
    if (annotations == null || annotations.length == 0) {
      throw new ExactSqlLogAgentStartException("annotations should has 1 element at least");
    } else {
      this.annotationList = ArrayUtils.asArrayList(annotations);
    }
  }

  @Override
  public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
    ElementMatcher.Junction<? super MethodDescription> junction = null;
    for (String annotation : annotationList) {
      if (junction == null) {
        junction = buildEachAnnotation(annotation);
      } else {
        junction = junction.and(buildEachAnnotation(annotation));
      }
    }
    return declaresMethod(junction)
            .and(ElementMatchers.not(isInterface()));
  }

  @Override
  public boolean isMatch(TypeDescription typeDescription) {
    for (MethodDescription.InDefinedShape methodDescription : typeDescription.getDeclaredMethods()) {
      List<String> annotationListCopy = new ArrayList<>(annotationList);

      AnnotationList declaredAnnotations = methodDescription.getDeclaredAnnotations();
      for (AnnotationDescription annotation : declaredAnnotations) {
        annotationListCopy.remove(annotation.getAnnotationType().getActualName());
      }
      if (annotationListCopy.isEmpty()) {
        return true;
      }
    }

    return false;
  }

  private ElementMatcher.Junction<? super MethodDescription> buildEachAnnotation(String annotationName) {
    return isAnnotatedWith(named(annotationName));
  }

  public static MethodAnnotationMatch of(String... annotations) {
    return new MethodAnnotationMatch(annotations);
  }

}
