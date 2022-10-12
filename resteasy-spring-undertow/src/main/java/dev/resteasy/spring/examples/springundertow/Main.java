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

import java.util.function.Supplier;

import org.jboss.resteasy.plugins.server.undertow.spring.UndertowJaxrsSpringServer;

import io.undertow.servlet.api.DeploymentInfo;

public class Main implements Supplier<UndertowJaxrsSpringServer> {
    public static void main(String[] args) throws Exception {
        final Main main = new Main();
        UndertowJaxrsSpringServer server = main.get();
        server.start();

        System.out.println("UNDERTOW SERVER STARTED");

        Thread.currentThread().join();

    }

    @Override
    public UndertowJaxrsSpringServer get() {
        UndertowJaxrsSpringServer server = new UndertowJaxrsSpringServer();

        DeploymentInfo deployment = server.undertowDeployment("classpath:resteasy-spring-mvc-servlet.xml", null);
        deployment.setDeploymentName(Main.class.getName());
        deployment.setContextPath("/");
        deployment.setClassLoader(Main.class.getClassLoader());
        server.deploy(deployment);
        return server;
    }
}
