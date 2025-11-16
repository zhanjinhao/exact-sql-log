package cn.addenda.exactsqllog.proxy.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtils {

  public static boolean ifQuerySql(String sql) {
    int length = sql.length();
    int i = 0;
    while (i < length) {
      if (sql.charAt(i) <= ' ') {
        i++;
      } else {
        break;
      }
    }

    StringBuilder stringBuilder = new StringBuilder();
    while (i < length) {
      char c = sql.charAt(i);
      if (c > ' ') {
        stringBuilder.append(c);
        i++;
      } else {
        break;
      }
    }

    return "select".equalsIgnoreCase(stringBuilder.toString());
  }

}
