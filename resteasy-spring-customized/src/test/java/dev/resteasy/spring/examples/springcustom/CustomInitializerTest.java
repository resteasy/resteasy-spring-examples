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

package dev.resteasy.spring.examples.springcustom;

import java.io.File;
import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
public class CustomInitializerTest {

    @ArquillianResource
    private URI uri;

    @Deployment
    public static WebArchive deployment() {
        final PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
        return ShrinkWrap.create(WebArchive.class, CustomInitializerTest.class.getSimpleName() + ".war")
                .addClasses(FooResource.class, RestActivator.class, MyConfig.class, MyContextLoaderListener.class,
                        MyWebAppInitializer.class)
                .addAsWebInfResource(new File(System.getProperty("web.inf.dir"), "beans.xml"), "beans.xml")
                .addAsLibraries(resolver.resolve("org.springframework:spring-webmvc").withTransitivity().asFile())
                .addAsLibrary(
                        resolver.resolve("org.jboss.resteasy.spring:resteasy-spring").withoutTransitivity().asSingleFile());
    }

    @Test
    public void servlet() {
        try (Client client = ClientBuilder.newClient()) {
            final Response response = client.target(UriBuilder.fromUri(uri).path("/rest/servlet")).request().get();
            final String result = response.readEntity(String.class);
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), result);
            Assertions.assertTrue(result.startsWith("org.jboss.resteasy.core.SynchronousDispatcher"),
                    () -> String.format("Result did not start with org.jboss.resteasy.core.SynchronousDispatcher: %s", result));
        }
    }
}
