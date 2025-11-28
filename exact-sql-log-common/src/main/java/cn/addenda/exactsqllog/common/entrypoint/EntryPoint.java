package cn.addenda.exactsqllog.common.entrypoint;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class EntryPoint {

  private EntryPointType entryPointType;

  private String detail;

  private EntryPoint(EntryPointType entryPointType, String detail) {
    this.entryPointType = entryPointType;
    this.detail = detail;
  }

  public static EntryPoint of(EntryPointType entryPointType, String detail) {
    return new EntryPoint(entryPointType, detail);
  }

  public static EntryPoint of(EntryPoint entryPoint) {
    return new EntryPoint(entryPoint.getEntryPointType(), entryPoint.getDetail());
  }

}
