package cn.addenda.exactsqllog.ext.json.serialzer.key;

import cn.addenda.exactsqllog.common.util.DateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;

import java.time.LocalDate;

/**
 * @author addenda
 * @since 2021/9/13
 */
public class LocalDateStrKeySerializer extends JsonSerializer<LocalDate> {

  @Override
  @SneakyThrows
  public void serialize(LocalDate localDate, JsonGenerator jgen, SerializerProvider provider) {
    if (localDate == null) {
      jgen.writeFieldName("null");
      return;
    }
    jgen.writeFieldName(DateUtils.format(localDate, DateUtils.yMd_FORMATTER));
  }

}
