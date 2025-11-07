package cn.addenda.exactsqllog.ext.json.deserialzer;

import cn.addenda.exactsqllog.common.util.DateUtils;
import cn.addenda.exactsqllog.common.util.StringUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateTimeStrDeSerializer extends JsonDeserializer<LocalDateTime> {

  @Override
  @SneakyThrows
  public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) {
    JsonNode jsonNode = jp.getCodec().readTree(jp);
    final String s = jsonNode.asText();
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    if (StringUtils.checkIsDigit(s) && s.length() > 8) {
      return DateUtils.timestampToLocalDateTime(Long.parseLong(s));
    }
    String yyyy = s.length() >= 4 ? s.substring(0, 4) : null;
    String MM = s.length() >= 7 ? s.substring(5, 7) : null;
    String dd = s.length() >= 10 ? s.substring(8, 10) : null;
    String HH = s.length() >= 13 ? s.substring(11, 13) : null;
    String mm = s.length() >= 16 ? s.substring(14, 16) : null;
    String ss = s.length() >= 19 ? s.substring(17, 19) : "00";
    String SSS = s.length() >= 23 ? s.substring(20, 23) : "000";
    if (yyyy == null || MM == null || dd == null || HH == null || mm == null) {
      throw new IllegalArgumentException(s);
    }
    return DateUtils.parseLdt(yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm + ":" + ss + "." + SSS, DateUtils.yMdHmsS_FORMATTER);
  }

}
