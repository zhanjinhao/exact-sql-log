package cn.addenda.exactsqllog.server.controller;

import cn.addenda.component.base.jackson.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("execution")
@Slf4j
public class ExecutionController {

  @PostMapping("receiveExecution")
  public void receiveExecution(@RequestBody String execution) {
    log.info("{}", JacksonUtils.toStr(execution));
  }

}
