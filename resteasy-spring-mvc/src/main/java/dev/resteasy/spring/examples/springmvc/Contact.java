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

import java.util.Objects;

import jakarta.ws.rs.FormParam;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Contact {
    private String firstName, lastName;

    public Contact() {
    }

    public Contact(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @FormParam("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @FormParam("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean equals(Object other) {
        // normal checks apply here...
        Contact otherContact = (Contact) other;
        return otherContact.firstName.equals(this.firstName)
                && otherContact.lastName.equals(this.lastName);
    }

    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }
}