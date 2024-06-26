/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.core;


/*
 * Vert.x hates Java checked exceptions and doesn't want to pollute it's API with them.
 * <p>
 * This is a general purpose exception class that is often thrown from Vert.x APIs if things go wrong.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 *
 */
public class VertxException extends RuntimeException {

  /**
   * Create an exception that does not capture a stack trace.
   *
   * @param msg the message
   * @return the created exception
   */
  public static VertxException noStackTrace(String msg) {
    return new VertxException(msg, true);
  }

  /**
   * Create an exception that does not capture a stack trace.
   *
   * @param msg the message
   * @param cause the cause
   * @return the created exception
   */
  public static VertxException noStackTrace(String msg, Throwable cause) {
    return new VertxException(msg, cause, true);
  }

  /**
   * Create an exception that does not capture a stack trace.
   *
   * @param cause the cause
   * @return the created exception
   */
  public static VertxException noStackTrace(Throwable cause) {
    return new VertxException(cause, true);
  }
  /**
   * Create an instance given a message
   *
   * @param message  the message
   */
  public VertxException(String message) {
    super(message);
  }

  /**
   * Create an instance given a message and a cause
   *
   * @param message  the message
   * @param cause  the cause
   */
  public VertxException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create an instance given a cause
   *
   * @param cause  the cause
   */
  public VertxException(Throwable cause) {
    super(cause);
  }

  /**
   * Create an instance given a message
   *
   * @param message  the message
   * @param noStackTrace  disable stack trace capture
   */
  public VertxException(String message, boolean noStackTrace) {
    super(message, null, !noStackTrace, !noStackTrace);
  }

  /**
   * Create an instance given a message
   *
   * @param message  the message
   * @param cause  the cause
   * @param noStackTrace  disable stack trace capture
   */
  public VertxException(String message, Throwable cause, boolean noStackTrace) {
    super(message, cause, !noStackTrace, !noStackTrace);
  }

  /**
   * Create an instance given a message
   *
   * @param cause  the cause
   * @param noStackTrace  disable stack trace capture
   */
  public VertxException(Throwable cause, boolean noStackTrace) {
    super(null, cause, !noStackTrace, !noStackTrace);
  }
}
