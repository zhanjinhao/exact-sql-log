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

package org.slf4j.impl;

import cn.addenda.exactsqllog.facade.LogFacade;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * simple adapter {@link Logger}
 */
public class Slf4jLoggerAdapter implements Logger {

  private final LogFacade log;
  private final String name;

  public Slf4jLoggerAdapter(LogFacade log, String name) {
    this.log = log;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isTraceEnabled() {
    return log.isTraceEnabled();
  }

  @Override
  public void trace(final String msg) {
    log.trace(msg);
  }

  @Override
  public void trace(final String format, final Object arg) {
    log.trace(format, arg);
  }

  @Override
  public void trace(final String format, final Object arg1, final Object arg2) {
    log.trace(format, arg1, arg2);
  }

  @Override
  public void trace(final String format, final Object... arguments) {
    log.trace(format, arguments);
  }

  @Override
  public void trace(final String msg, final Throwable t) {
    log.trace(msg, t);
  }

  @Override
  public boolean isTraceEnabled(final Marker marker) {
    return log.isTraceEnabled();
  }

  @Override
  public void trace(final Marker marker, final String msg) {
    log.trace(msg);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg) {
    log.trace(format, arg);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
    log.trace(format, arg1, arg2);
  }

  @Override
  public void trace(final Marker marker, final String format, final Object... arguments) {
    log.trace(format, arguments);
  }

  @Override
  public void trace(final Marker marker, final String msg, final Throwable t) {
    log.trace(msg, t);
  }

  @Override
  public boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  @Override
  public void debug(final String msg) {
    log.debug(msg);
  }

  @Override
  public void debug(final String format, final Object arg) {
    log.debug(format, arg);
  }

  @Override
  public void debug(final String format, final Object arg1, final Object arg2) {
    log.debug(format, arg1, arg2);
  }

  @Override
  public void debug(final String format, final Object... arguments) {
    log.debug(format, arguments);
  }

  @Override
  public void debug(final String msg, final Throwable t) {
    log.debug(msg, t);
  }

  @Override
  public boolean isDebugEnabled(final Marker marker) {
    return log.isDebugEnabled();
  }

  @Override
  public void debug(final Marker marker, final String msg) {
    log.debug(msg);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg) {
    log.debug(format, arg);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
    log.debug(format, arg1, arg2);
  }

  @Override
  public void debug(final Marker marker, final String format, final Object... arguments) {
    log.debug(format, arguments);
  }

  @Override
  public void debug(final Marker marker, final String msg, final Throwable t) {
    log.debug(msg, t);
  }

  @Override
  public boolean isInfoEnabled() {
    return log.isInfoEnabled();
  }

  @Override
  public void info(final String msg) {
    log.info(msg);
  }

  @Override
  public void info(final String format, final Object arg) {
    log.info(format, arg);
  }

  @Override
  public void info(final String format, final Object arg1, final Object arg2) {
    log.info(format, arg1, arg2);
  }

  @Override
  public void info(final String format, final Object... arguments) {
    log.info(format, arguments);
  }

  @Override
  public void info(final String msg, final Throwable t) {
    log.info(msg, t);
  }

  @Override
  public boolean isInfoEnabled(final Marker marker) {
    return log.isInfoEnabled();
  }

  @Override
  public void info(final Marker marker, final String msg) {
    log.info(msg);
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg) {
    log.info(format, arg);
  }

  @Override
  public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
    log.info(format, arg1, arg2);
  }

  @Override
  public void info(final Marker marker, final String format, final Object... arguments) {
    log.info(format, arguments);
  }

  @Override
  public void info(final Marker marker, final String msg, final Throwable t) {
    log.info(msg, t);
  }

  @Override
  public boolean isWarnEnabled() {
    return log.isWarnEnabled();
  }

  @Override
  public void warn(final String msg) {
    log.warn(msg);
  }

  @Override
  public void warn(final String format, final Object arg) {
    log.warn(format, arg);
  }

  @Override
  public void warn(final String format, final Object... arguments) {
    log.warn(format, arguments);
  }

  @Override
  public void warn(final String format, final Object arg1, final Object arg2) {
    log.warn(format, arg1, arg2);
  }

  @Override
  public void warn(final String msg, final Throwable t) {
    log.warn(msg, t);
  }

  @Override
  public boolean isWarnEnabled(final Marker marker) {
    return log.isWarnEnabled();
  }

  @Override
  public void warn(final Marker marker, final String msg) {
    log.warn(msg);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg) {
    log.warn(format, arg);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
    log.warn(format, arg1, arg2);
  }

  @Override
  public void warn(final Marker marker, final String format, final Object... arguments) {
    log.warn(format, arguments);
  }

  @Override
  public void warn(final Marker marker, final String msg, final Throwable t) {
    log.warn(msg, t);
  }

  @Override
  public boolean isErrorEnabled() {
    return log.isErrorEnabled();
  }

  @Override
  public void error(final String msg) {
    log.error(msg);
  }

  @Override
  public void error(final String format, final Object arg) {
    log.error(format, arg);
  }

  @Override
  public void error(final String format, final Object arg1, final Object arg2) {
    log.error(format, arg1, arg2);
  }

  @Override
  public void error(final String format, final Object... arguments) {
    log.error(format, arguments);
  }

  @Override
  public void error(final String msg, final Throwable t) {
    log.error(msg, t);
  }

  @Override
  public boolean isErrorEnabled(final Marker marker) {
    return log.isErrorEnabled();
  }

  @Override
  public void error(final Marker marker, final String msg) {
    log.error(msg);
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg) {
    log.error(format, arg);
  }

  @Override
  public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
    log.error(format, arg1, arg2);
  }

  @Override
  public void error(final Marker marker, final String format, final Object... arguments) {
    log.error(format, arguments);
  }

  @Override
  public void error(final Marker marker, final String msg, final Throwable t) {
    log.error(msg, t);
  }

}
