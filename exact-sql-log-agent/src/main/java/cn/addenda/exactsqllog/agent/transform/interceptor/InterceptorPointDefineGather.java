package cn.addenda.exactsqllog.agent.transform.interceptor;

import cn.addenda.exactsqllog.agent.transform.match.ClassMatch;
import cn.addenda.exactsqllog.agent.transform.match.IndirectMatch;
import cn.addenda.exactsqllog.agent.transform.match.NameMatch;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.*;

public class InterceptorPointDefineGather {

  private final Map<String, LinkedList<InterceptorPointDefine>> interceptorPointDefineMap = new HashMap<>();

  private final List<InterceptorPointDefine> signatureMatchDefineList = new ArrayList<>();

  public void addInterceptorPointDefine(InterceptorPointDefine interceptorPointDefine) {
    ClassMatch classMatch = interceptorPointDefine.getEnhancedClass();
    if (classMatch == null) {
      return;
    }
    if (classMatch instanceof NameMatch) {
      NameMatch nameMatch = (NameMatch) classMatch;
      LinkedList<InterceptorPointDefine> list = interceptorPointDefineMap.computeIfAbsent(nameMatch.getClassName(), s -> new LinkedList<>());
      list.add(interceptorPointDefine);
    } else {
      signatureMatchDefineList.add(interceptorPointDefine);
    }
  }

  /**
   * @return plugin1_junction or plugin2_junction or plugin3_junction
   */
  public ElementMatcher.Junction<? super TypeDescription> buildMatch() {
    ElementMatcher.Junction<? super TypeDescription> junction =
            new ElementMatcher.Junction.AbstractBase<NamedElement>() {
              @Override
              public boolean matches(NamedElement target) {
                return interceptorPointDefineMap.containsKey(target.getActualName());
              }
            };
    junction = junction.and(ElementMatchers.not(ElementMatchers.isInterface()));

    for (InterceptorPointDefine interceptorPointDefine : signatureMatchDefineList) {
      ClassMatch classMatch = interceptorPointDefine.getEnhancedClass();
      if (classMatch instanceof IndirectMatch) {
        IndirectMatch indirectMatch = (IndirectMatch) classMatch;
        junction = junction.or(indirectMatch.buildJunction());
      }
    }

    return junction;
  }

  public List<InterceptorPointDefine> find(TypeDescription typeDescription) {
    List<InterceptorPointDefine> interceptorPointDefineList = new ArrayList<>();
    String typeName = typeDescription.getTypeName();

    if (interceptorPointDefineMap.containsKey(typeName)) {
      interceptorPointDefineList.addAll(interceptorPointDefineMap.get(typeName));
    }

    for (InterceptorPointDefine interceptorPointDefine : signatureMatchDefineList) {
      IndirectMatch indirectMatch = (IndirectMatch) interceptorPointDefine.getEnhancedClass();
      if (indirectMatch.isMatch(typeDescription)) {
        interceptorPointDefineList.add(interceptorPointDefine);
      }
    }

    return interceptorPointDefineList;
  }

}
