package cn.addenda.exactsqllog.common.pojo;

import lombok.*;

import java.util.Objects;

/**
 * 三元
 *
 * @author addenda
 * @since 2024/1/22 13:46
 */
@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Ternary<T1, T2, T3> {

  private T1 f1;
  private T2 f2;
  private T3 f3;

  @Override
  public boolean equals(Object source) {
    if (this == source) return true;
    if (source == null || getClass() != source.getClass()) return false;
    Ternary<?, ?, ?> ternary = (Ternary<?, ?, ?>) source;
    return Objects.equals(f1, ternary.f1)
            && Objects.equals(f2, ternary.f2)
            && Objects.equals(f3, ternary.f3);
  }

  @Override
  public int hashCode() {
    return Objects.hash(f1, f2, f3);
  }

  public static <T1, T2, T3> Ternary<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
    return new Ternary<>(t1, t2, t3);
  }

}
