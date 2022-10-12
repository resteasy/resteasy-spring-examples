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

import java.net.URI;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import org.jboss.resteasy.annotations.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Path(ContactsResource.CONTACTS_URL)
public class ContactsResource {
    public static final String CONTACTS_URL = "/rest/contacts";
    @Autowired
    ContactService service;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("data")
    public Contacts getAll() {
        return service.getAll();
    }

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("data")
    public Response saveContact(Contact contact) {
        service.save(contact);
        URI newURI = UriBuilder.fromUri(uriInfo.getPath()).path(contact.getLastName()).build();
        return Response.created(newURI).build();
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("data/{lastName}")
    public Contact get(@PathParam("lastName") String lastName) {
        return service.getContact(lastName);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public ModelAndView saveContactForm(@Form Contact contact) {
        service.save(contact);
        return viewAll();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public ModelAndView viewAll() {
        // forward to the "contacts" view, with a request attribute named
        // "contacts" that has all of the existing contacts
        return new ModelAndView("contacts", "contacts", service.getAll());
    }
}
