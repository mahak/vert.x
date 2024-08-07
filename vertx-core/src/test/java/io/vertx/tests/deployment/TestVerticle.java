/*
 * Copyright (c) 2011-2023 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.tests.deployment;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class TestVerticle extends AbstractVerticle {

  public static AtomicInteger instanceCount = new AtomicInteger();
  public static JsonObject conf;

  public TestVerticle() {
  }

  @Override
  public void start() throws Exception {
    conf = context.config();
    vertx.eventBus().send("testcounts",
      new JsonObject().put("deploymentID", context.deploymentID()).put("count", instanceCount.incrementAndGet()));
  }

  @Override
  public void stop() throws Exception {
  }

}
