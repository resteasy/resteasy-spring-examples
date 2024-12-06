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

package dev.resteasy.spring.examples.springrest;

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
public class SpringRestTest {

    @ArquillianResource
    private URI uri;

    @Deployment
    public static WebArchive deployment() {
        final PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
        return ShrinkWrap.create(WebArchive.class, SpringRestTest.class.getSimpleName() + ".war")
                .addClasses(MyApplication.class, SpringRestAnnotationResource.class)
                .addAsWebInfResource(new File(System.getProperty("web.inf.dir"), "web.xml"), "web.xml")
                .addAsLibraries(resolver.resolve("org.springframework:spring-web").withTransitivity().asFile())
                .addAsLibraries(resolver.resolve("org.springframework:spring-context").withTransitivity().asFile())
                .addAsLibraries(
                        resolver.resolve("org.jboss.resteasy.spring:resteasy-spring-web").withoutTransitivity().asFile());
    }

    @Test
    public void springRest() {
        try (Client client = ClientBuilder.newClient()) {
            final Response response = client.target(UriBuilder.fromUri(uri).path("/spring")).request().get();
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo());
            Assertions.assertEquals("Spring is coming!", response.readEntity(String.class));
        }
    }
}
