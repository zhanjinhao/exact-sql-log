package cn.addenda.exactsqllog.ext.json.deserialzer.key;

import cn.addenda.exactsqllog.common.util.DateUtils;
import cn.addenda.exactsqllog.common.util.StringUtils;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author addenda
 * @since 2021/9/13
 */
@Slf4j
public class LocalDateStrKeyDeSerializer extends KeyDeserializer {

  @Override
  @SneakyThrows
  public Object deserializeKey(String s, DeserializationContext ctxt) {
    if (s == null || s.isEmpty() || "null".equals(s)) {
      return null;
    }
    if (StringUtils.checkIsDigit(s) && s.length() > 8) {
      return DateUtils.timestampToLocalDateTime(Long.parseLong(s)).toLocalDate();
    }
    return DateUtils.parseLd(s, DateUtils.yMd_FORMATTER);
  }

}
