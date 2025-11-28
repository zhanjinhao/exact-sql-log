package cn.addenda.exactsqllog.server.controller;

import cn.addenda.component.base.jackson.util.JacksonUtils;
import cn.addenda.exactsqllog.common.bo.PreparedSqlBo;
import cn.addenda.exactsqllog.common.bo.SqlBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("execution")
@Slf4j
public class ExecutionController {

  @PostMapping("receivePreparedSqlBo")
  public void receivePreparedSqlBo(@RequestBody PreparedSqlBo preparedSqlBo) {
    log.info("{}", JacksonUtils.toStr(preparedSqlBo));
  }

  @PostMapping("receiveSqlBo")
  public void receiveSqlBo(@RequestBody SqlBo sqlBo) {
    log.info("{}", JacksonUtils.toStr(sqlBo));
  }

}
