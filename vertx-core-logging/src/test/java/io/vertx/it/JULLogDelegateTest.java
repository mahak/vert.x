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

package io.vertx.it;

import io.vertx.core.internal.logging.Logger;
import io.vertx.core.internal.logging.LoggerAdapter;
import io.vertx.core.internal.logging.LoggerFactory;
import io.vertx.core.spi.logging.LogDelegate;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * These tests check the JUL log delegate. It analyses the output, so any change in the configuration may break the
 * tests.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class JULLogDelegateTest {

  private static Logger logger;
  private static Recording recording;

  @BeforeClass
  public static void initialize() throws IOException {
    // Init logging before recording add its own logging handler
    logger = LoggerFactory.getLogger("my-jul-logger");
    recording = new Recording();
  }

  @Test
  public void testDelegateUnwrap() {
    LogDelegate delegate = ((LoggerAdapter) logger).unwrap();
    assertNotNull("Delegate is null", delegate);
    try {
      java.util.logging.Logger unwrapped = (java.util.logging.Logger) delegate.unwrap();
      assertNotNull("Unwrapped is null", unwrapped);
    } catch (ClassCastException e) {
      fail("Unexpected unwrapped type: " + e.getMessage());
    }
  }

  @Test
  public void testInfo() {
    String result = recording.execute(() -> {
      Logger logger = LoggerFactory.getLogger("my-jul-logger");
      logger.info("hello");
    });
    assertTrue(result.contains("hello"));
    result = recording.execute(() -> {
      Logger logger = LoggerFactory.getLogger("my-jul-logger");
      logger.info("exception", new NullPointerException());
    });

    assertTrue(result.contains("exception"));
    assertTrue(result.contains("java.lang.NullPointerException"));
  }

  @Test
  public void testError() {
    String result = recording.execute(() -> {
      Logger logger = LoggerFactory.getLogger("my-jul-logger");
      logger.error("hello");
    });
    assertTrue(result.contains("hello"));
    result = recording.execute(() -> {
      Logger logger = LoggerFactory.getLogger("my-jul-logger");
      logger.error("exception", new NullPointerException());
    });
    assertTrue(result.contains("exception"));
    assertTrue(result.contains("java.lang.NullPointerException"));
  }

  @Test
  public void testWarning() {
    String result = recording.execute(() -> {
      Logger logger = LoggerFactory.getLogger("my-jul-logger");
      logger.warn("hello");
    });
    assertTrue(result.contains("hello"));
    result = recording.execute(() -> {
      Logger logger = LoggerFactory.getLogger("my-jul-logger");
      logger.warn("exception", new NullPointerException());
    });
    assertTrue(result.contains("exception"));
    assertTrue(result.contains("java.lang.NullPointerException"));
  }
}
