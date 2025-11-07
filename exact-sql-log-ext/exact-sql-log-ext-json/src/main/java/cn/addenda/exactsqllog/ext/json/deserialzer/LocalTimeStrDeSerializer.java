package cn.addenda.exactsqllog.ext.json.deserialzer;

import cn.addenda.exactsqllog.common.util.DateUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import java.time.LocalTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalTimeStrDeSerializer extends JsonDeserializer<LocalTime> {

  @Override
  @SneakyThrows
  public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt) {
    JsonNode jsonNode = jp.getCodec().readTree(jp);
    final String s = jsonNode.asText();
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    String HH = s.length() >= 2 ? s.substring(0, 2) : null;
    String mm = s.length() >= 5 ? s.substring(3, 5) : null;
    String ss = s.length() >= 8 ? s.substring(6, 8) : "00";
    String SSS = s.length() >= 12 ? s.substring(9, 12) : "000";
    if (HH == null || mm == null) {
      throw new IllegalArgumentException(s);
    }
    return DateUtils.parseLt(HH + ":" + mm + ":" + ss + "." + SSS, DateUtils.HmsS_FORMATTER);
  }

}
