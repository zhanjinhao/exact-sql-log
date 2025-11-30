package cn.addenda.exactsqllog.server.controller;

import cn.addenda.exactsqllog.common.bo.PreparedSqlBo;
import cn.addenda.exactsqllog.common.bo.SqlBo;
import cn.addenda.exactsqllog.ext.json.EslJsonUtils;
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
    log.info("{}", EslJsonUtils.toStr(preparedSqlBo));
  }

  @PostMapping("receiveSqlBo")
  public void receiveSqlBo(@RequestBody SqlBo sqlBo) {
    log.info("{}", EslJsonUtils.toStr(sqlBo));
  }

}
