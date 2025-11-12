package cn.addenda.exactsqllog.agent.transform;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.*;

public class InterceptorPointDefineGather {

  private final Map<String, LinkedList<InterceptorPointDefine>> interceptorPointDefineMap = new HashMap<>();

  public void addInterceptorPointDefine(InterceptorPointDefine interceptorPointDefine) {
    LinkedList<InterceptorPointDefine> interceptorPointDefines =
            interceptorPointDefineMap.computeIfAbsent(interceptorPointDefine.getEnhancedClassName(), s -> new LinkedList<>());
    interceptorPointDefines.add(interceptorPointDefine);
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
    return junction;
  }

  public List<InterceptorPointDefine> find(TypeDescription typeDescription) {
    List<InterceptorPointDefine> matchedInterceptorPointDefineList = new ArrayList<>();
    String typeName = typeDescription.getTypeName();

    if (interceptorPointDefineMap.containsKey(typeName)) {
      matchedInterceptorPointDefineList.addAll(interceptorPointDefineMap.get(typeName));
    }

    return matchedInterceptorPointDefineList;
  }

}
