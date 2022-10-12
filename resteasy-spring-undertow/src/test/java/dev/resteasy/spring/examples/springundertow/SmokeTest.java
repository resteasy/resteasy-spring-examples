/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2022 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.resteasy.spring.examples.springundertow;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

import org.jboss.resteasy.plugins.server.undertow.spring.UndertowJaxrsSpringServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SmokeTest {

    private static UndertowJaxrsSpringServer SERVER;

    @BeforeAll
    public static void startServer() {
        final Main main = new Main();
        SERVER = main.get();
        SERVER.start();
    }

    @AfterAll
    public static void stopServer() {
        if (SERVER != null) {
            SERVER.stop();
        }
    }

    @Test
    public void test() throws Exception {
        try (Client client = ClientBuilder.newClient()) {
            // Access server and get expected result
            Assertions.assertEquals("Hello, world!",
                    client
                            .target("http://localhost:8081/rest/foo")
                            .request()
                            .get(String.class));
        }
    }
}
