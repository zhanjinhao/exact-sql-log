package cn.addenda.exactsqllog.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author addenda
 * @since 2022/2/7 12:37
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

  public static final String yMdHmsS_FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String yMdHms_FORMATTER = "yyyy-MM-dd HH:mm:ss";
  public static final String yMdHm_FORMATTER = "yyyy-MM-dd HH:mm";
  public static final String yMd_FORMATTER = "yyyy-MM-dd";
  public static final String HmsS_FORMATTER = "HH:mm:ss.SSS";
  public static final String Hms_FORMATTER = "HH:mm:ss";
  public static final String Hm_FORMATTER = "HH:mm";
  private static final Map<String, DateTimeFormatter> FORMATTER_MAP = new ConcurrentHashMap<>();

  // 这个可以从配置文件读，暂时没必要。。。
  private static ZoneId defaultZoneId = ZoneId.systemDefault();

  public static LocalDateTime dateToLocalDateTime(Date date) {
    return dateToLocalDateTime(date, defaultZoneId);
  }

  public static LocalDateTime dateToLocalDateTime(Date date, ZoneId zoneId) {
    if (date == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    Instant instant = date.toInstant();
    return LocalDateTime.ofInstant(instant, zoneId);
  }

  public static Long localDateTimeToTimestamp(LocalDateTime localDateTime) {
    return localDateTimeToTimestamp(localDateTime, defaultZoneId);
  }

  public static Long localDateTimeToTimestamp(LocalDateTime localDateTime, ZoneId zoneId) {
    if (localDateTime == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
  }

  public static LocalDateTime timestampToLocalDateTime(Long timestamp) {
    return timestampToLocalDateTime(timestamp, defaultZoneId);
  }

  public static LocalDateTime timestampToLocalDateTime(Long timestamp, ZoneId zoneId) {
    if (timestamp == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    Instant instant = Instant.ofEpochMilli(timestamp);
    return LocalDateTime.ofInstant(instant, zoneId);
  }

  public static Date localDateTimeToDate(LocalDateTime localDateTime) {
    return localDateTimeToDate(localDateTime, defaultZoneId);
  }

  public static Date localDateTimeToDate(LocalDateTime localDateTime, ZoneId zoneId) {
    if (localDateTime == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Instant instant = zonedDateTime.toInstant();
    return Date.from(instant);
  }

  public static Date localDateToDate(LocalDate localDate) {
    return localDateToDate(localDate, defaultZoneId);
  }

  public static Date localDateToDate(LocalDate localDate, ZoneId zoneId) {
    if (localDate == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    LocalDateTime localDateTime = localDate.atTime(0, 0, 0);
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Instant instant = zonedDateTime.toInstant();
    return Date.from(instant);
  }

  public static Date localTimeToDate(LocalTime localTime) {
    return localTimeToDate(localTime, defaultZoneId);
  }

  public static Date localTimeToDate(LocalTime localTime, ZoneId zoneId) {
    if (localTime == null) {
      return null;
    }
    if (zoneId == null) {
      zoneId = defaultZoneId;
    }
    LocalDateTime localDateTime = localTime.atDate(LocalDate.of(1970, 1, 1));
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Instant instant = zonedDateTime.toInstant();
    return Date.from(instant);
  }

  public static String format(LocalDateTime localDateTime, String formatter) {
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return dateTimeFormatter.format(localDateTime);
  }

  public static String format(LocalDate localDate, String formatter) {
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return dateTimeFormatter.format(localDate);
  }

  public static String format(LocalTime localTime, String formatter) {
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return dateTimeFormatter.format(localTime);
  }

  public static LocalDateTime parseLdt(String localDateTime, String formatter) {
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return LocalDateTime.parse(localDateTime, dateTimeFormatter);
  }

  public static LocalDate parseLd(String localDate, String formatter) {
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return LocalDate.parse(localDate, dateTimeFormatter);
  }

  public static LocalTime parseLt(String localTime, String formatter) {
    DateTimeFormatter dateTimeFormatter =
            FORMATTER_MAP.computeIfAbsent(formatter, s -> DateTimeFormatter.ofPattern(formatter));
    return LocalTime.parse(localTime, dateTimeFormatter);
  }

}
