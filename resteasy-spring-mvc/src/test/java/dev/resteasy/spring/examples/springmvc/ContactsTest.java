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

package dev.resteasy.spring.examples.springmvc;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContactsTest {

    @ArquillianResource
    private URI uri;

    @Deployment
    public static WebArchive deployment() {
        final PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
        return ShrinkWrap.create(WebArchive.class, ContactsTest.class.getSimpleName() + ".war")
                .addClasses(Contact.class, Contacts.class, ContactService.class, ContactsResource.class)
                .addAsWebInfResource("resteasy-spring-mvc-servlet.xml", "classes/resteasy-spring-mvc-servlet.xml")
                .addAsWebInfResource(new File(System.getProperty("web.inf.dir"), "web.xml"), "web.xml")
                .addAsLibraries(resolver.resolve("org.springframework:spring-webmvc").withTransitivity().asFile())
                .addAsLibrary(
                        resolver.resolve("org.jboss.resteasy.spring:resteasy-spring").withoutTransitivity().asSingleFile());
    }

    @Test
    @Order(1)
    public void saveContact() {
        try (Client client = ClientBuilder.newClient()) {
            final Contact contact = new Contact("RESTEasy", "Spring");
            try (Response response = client.target(UriBuilder.fromUri(uri).path("/rest/contacts/data"))
                    .request(MediaType.APPLICATION_XML_TYPE).post(Entity.entity(contact, MediaType.APPLICATION_XML_TYPE))) {
                Assertions.assertEquals(Response.Status.CREATED, response.getStatusInfo(),
                        () -> response.readEntity(String.class));
                final URI createdUri = response.getLocation();
                Assertions.assertEquals(UriBuilder.fromUri(uri).path("/rest/contacts/data/Spring").build(),
                        createdUri);
            }
        }
    }

    @Test
    @Order(2)
    public void allContacts() {
        try (Client client = ClientBuilder.newClient()) {
            final Response response = client.target(UriBuilder.fromUri(uri).path("/rest/contacts/data"))
                    .request(MediaType.APPLICATION_XML_TYPE).get();
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), () -> response.readEntity(String.class));
            final Contacts contacts = response.readEntity(Contacts.class);
            Assertions.assertNotNull(contacts);
            final Collection<Contact> allContacts = contacts.getContacts();
            Assertions.assertEquals(1, allContacts.size());
            final Contact contact = allContacts.stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed to find contact: " + contacts));
            Assertions.assertEquals("RESTEasy", contact.getFirstName());
            Assertions.assertEquals("Spring", contact.getLastName());
        }
    }

    @Test
    @Order(3)
    public void getContact() {
        try (Client client = ClientBuilder.newClient()) {
            final Response response = client.target(UriBuilder.fromUri(uri).path("/rest/contacts/data/Spring"))
                    .request(MediaType.APPLICATION_XML_TYPE).get();
            Assertions.assertEquals(Response.Status.OK, response.getStatusInfo(), () -> response.readEntity(String.class));
            final Contact contact = response.readEntity(Contact.class);
            Assertions.assertEquals("RESTEasy", contact.getFirstName());
            Assertions.assertEquals("Spring", contact.getLastName());
        }
    }

}
