package cn.addenda.exactsqllog.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UuidUtils {

  public static String generateUuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }

}
