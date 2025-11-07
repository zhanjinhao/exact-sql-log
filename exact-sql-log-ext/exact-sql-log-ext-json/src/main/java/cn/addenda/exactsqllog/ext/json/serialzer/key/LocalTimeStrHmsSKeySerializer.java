package cn.addenda.exactsqllog.ext.json.serialzer.key;

import cn.addenda.exactsqllog.common.util.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

import java.time.LocalTime;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalTimeStrHmsSKeySerializer extends JsonSerializer<LocalTime> {

  @Override
  @SneakyThrows
  public void serialize(LocalTime localTime, JsonGenerator jgen, SerializerProvider provider) {
    if (localTime == null) {
      jgen.writeFieldName("null");
      return;
    }
    jgen.writeFieldName(DateUtils.format(localTime, DateUtils.HmsS_FORMATTER));
  }

}
