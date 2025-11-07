package cn.addenda.exactsqllog.common.pojo;

import lombok.*;

import java.util.Objects;

/**
 * 一元
 *
 * @author addenda
 * @since 2024/1/22 13:47
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Unary<T1> {

  private T1 f1;

  @Override
  public boolean equals(Object source) {
    if (this == source) return true;
    if (source == null || getClass() != source.getClass()) return false;
    Unary<?> unary = (Unary<?>) source;
    return Objects.equals(f1, unary.f1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1);
  }

  public static <T1> Unary<T1> of(T1 t1) {
    return new Unary<>(t1);
  }

}
