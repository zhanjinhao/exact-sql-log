package cn.addenda.exactsqllog.ext.json.serialzer;

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
public class LocalTimeStrHmsSSerializer extends JsonSerializer<LocalTime> {

  @Override
  @SneakyThrows
  public void serialize(LocalTime localTime, JsonGenerator jgen, SerializerProvider provider) {
    if (localTime == null) {
      jgen.writeString((String) null);
      return;
    }
    jgen.writeString(DateUtils.format(localTime, DateUtils.HmsS_FORMATTER));
  }

}
