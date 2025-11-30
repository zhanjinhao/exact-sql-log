package cn.addenda.exactsqllog.server.controller;

import cn.addenda.exactsqllog.common.config.EslConnectionConfig;
import cn.addenda.exactsqllog.ext.json.EslJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("eslConnectionConfig")
@Slf4j
public class EslConnectionConfigController {

  @PostMapping("receiveEslConnectionConfig")
  public void receiveExecution(@RequestBody EslConnectionConfig eslConnectionConfig) {
    log.info("{}", EslJsonUtils.toStr(eslConnectionConfig));
  }

}
