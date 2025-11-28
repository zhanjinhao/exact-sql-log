package cn.addenda.exactsqllog.agent.transform.match;

import lombok.Getter;

/**
 * 专门用于类名==xxx的情况
 */
public class NameMatch implements ClassMatch {

  @Getter
  private final String className;

  private NameMatch(String className) {
    this.className = className;
  }

  public static NameMatch of(String className) {
    return new NameMatch(className);
  }

}
