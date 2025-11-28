package cn.addenda.exactsqllog.common.entrypoint;

import lombok.Getter;

@Getter
public enum EntryPointType {

  HTTP_SPRING("HTTP_SPRING", 1),
  TX_TRANSACTIONAL("TX_TRANSACTIONAL", 2),
  RPC_DUBBO2("RPC_DUBBO2", 1),
  ORM_MYBATIS("ORM_MYBATIS", 3),

  REMOTE_JDBC("REMOTE_JDBC", 4);

  private final String type;

  private final Integer level;

  EntryPointType(String type, Integer level) {
    this.type = type;
    this.level = level;
  }

}