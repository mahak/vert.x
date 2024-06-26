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

package io.vertx.tests.net;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.test.core.Repeat;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * Orginial reproducer from https://github.com/LarsKrogJensen
 */
public class SharedServersConcurrencyTest extends VertxTestBase {

  @Test
  @Repeat(times = 100)
  public void testConcurrency() {
    vertx.deployVerticle(new MonitorVerticle())
      .compose(__ -> vertx.deployVerticle(new RestVerticle()))
      .compose(__ -> vertx.deployVerticle(new ApiVerticle()))
      .onComplete(onSuccess(__ -> testComplete()));
    await();
  }

  private static class ApiVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
      vertx.deployVerticle(() -> new NetVerticle(), new DeploymentOptions().setInstances(32)).onComplete(ar -> {
        if (ar.succeeded()) {
          startPromise.complete();
        } else {
          startPromise.fail(ar.cause());
        }
      });
    }
  }

  private static class NetVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
      vertx.createNetServer(new NetServerOptions().setPort(20152))
        .connectHandler(netSocket -> {
        })
        .listen().onComplete(ar -> {
          if (ar.succeeded()) {
            startPromise.complete();
          } else {
            startPromise.fail(ar.cause());
          }
        });
    }
  }

  private static class RestVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
      vertx.createHttpServer(new HttpServerOptions())
        .requestHandler(req -> {
        })
        .listen(15152).onComplete(ar -> {
          if (ar.succeeded()) {
            System.out.println("REST listening on port: " + ar.result().actualPort());
            startPromise.complete();
          } else {
            startPromise.fail(ar.cause());
          }
        });
    }
  }

  private static class MonitorVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
      vertx.createHttpServer(new HttpServerOptions())
        .requestHandler(req -> {
        })
        .listen(16152).onComplete(ar -> {
          if (ar.succeeded()) {
            System.out.println("Monitor listening on port: " + ar.result().actualPort());
            startPromise.complete();
          } else {
            startPromise.fail(ar.cause());
          }
        });
    }
  }
}
