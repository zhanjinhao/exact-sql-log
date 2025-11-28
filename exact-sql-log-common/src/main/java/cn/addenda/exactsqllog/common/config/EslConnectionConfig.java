package cn.addenda.exactsqllog.common.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EslConnectionConfig {

  private String jdbcUrl;

  private String user;

  private String password;

  private String eslConnectionId;

}
