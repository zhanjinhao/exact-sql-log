/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.addenda.exactsqllog.agent;

import cn.addenda.exactsqllog.agent.system.AgentDefaultSystemLoggerFactory;
import cn.addenda.exactsqllog.proxy.system.SystemLogger;
import net.bytebuddy.dynamic.DynamicType;

import java.io.File;
import java.io.IOException;

/**
 * The manipulated class output. Write the dynamic classes to the `debugging` folder, when we need to do some debug and
 * recheck.
 */
public enum InstrumentDebuggingClass {

  INSTANCE;

  private File debuggingClassesRootPath;

  private static final SystemLogger log = AgentDefaultSystemLoggerFactory.getInstance().getSystemLogger(InstrumentDebuggingClass.class);

  public void log(DynamicType dynamicType) {

    /**
     * try to do I/O things in synchronized way, to avoid unexpected situations.
     */
    synchronized (INSTANCE) {
      try {
        if (debuggingClassesRootPath == null) {
          debuggingClassesRootPath = new File(AgentPackage.getPath(), "/debugging");
          if (!debuggingClassesRootPath.exists()) {
            debuggingClassesRootPath.mkdir();
          }
        }

        try {
          dynamicType.saveIn(debuggingClassesRootPath);
        } catch (IOException e) {
          log.error("Can't save class {} to file." + dynamicType.getTypeDescription().getActualName(), e);
        }
      } catch (Throwable t) {
        log.error("Save debugging classes fail.", t);
      }
    }
  }

}
