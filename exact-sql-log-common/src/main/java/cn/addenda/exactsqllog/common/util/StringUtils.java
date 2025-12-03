package cn.addenda.exactsqllog.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author addenda
 * @since 2022/2/7 12:38
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

  public static String expandWithSpecifiedChar(String str, char specifiedChar, int expectLength) {
    int length = str.length();
    StringBuilder zero = new StringBuilder();
    for (int i = length; i < expectLength; i++) {
      zero.append(specifiedChar);
    }
    return zero.append(str).toString();
  }

  public static String expandWithZero(String str, int expectLength) {
    return expandWithSpecifiedChar(str, '0', expectLength);
  }

  public static String join(String separator, String... values) {
    if (values.length == 0) {
      return "";
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < values.length; i++) {
      if (i == 0) {
        result.append(!hasText(values[i]) ? "" : values[i]);
      } else {
        result.append(!hasText(values[i]) ? "" : separator + values[i]);
      }
    }
    return result.toString();
  }

  public static boolean checkIsDigit(String piece) {
    if (!hasText(piece)) {
      return false;
    }
    return piece.matches("\\d+");
  }

  public static String discardNull(String str) {
    if (str == null) {
      return "";
    }
    return str;
  }

  public static boolean hasText(CharSequence str) {
    return (str != null && str.length() > 0 && containsText(str));
  }

  public static boolean hasText(String str) {
    return str != null && !str.isEmpty() && containsText(str);
  }

  public static boolean containsText(CharSequence str) {
    int strLen = str.length();

    for (int i = 0; i < strLen; ++i) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }

    return false;
  }

  public static boolean hasLength(CharSequence str) {
    return (str != null && str.length() > 0);
  }

  public static String biTrimSpecifiedChar(String input, char c) {
    if (input == null) {
      return null;
    }

    int length = input.length();

    int start = 0;
    for (int i = 0; i < length; i++) {
      if (input.charAt(i) != c) {
        start = i;
        break;
      }
    }

    int end = length - 1;
    for (int i = length - 1; i >= 0; i--) {
      if (input.charAt(i) != c) {
        end = i;
        break;
      }
    }

    return input.substring(start, end + 1);
  }

  public static boolean isStrictlyNumeric(CharSequence cs) {
    if (cs == null || cs.length() == 0) {
      return false;
    }
    for (int i = 0; i < cs.length(); i++) {
      if (!Character.isDigit(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
    if (index + substring.length() > str.length()) {
      return false;
    }
    for (int i = 0; i < substring.length(); i++) {
      if (str.charAt(index + i) != substring.charAt(i)) {
        return false;
      }
    }
    return true;
  }

}
