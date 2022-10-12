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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Simple Service object. Really, this class isn't needed in this case. However
 * Controller/Service/Repository layering is a pretty common design pattern in
 * Spring projects. While this example doesn't have a Repository/DAO, the
 * ContactService class will show how to integrate Controllers with the layers
 * below in a Spring/RESTEasy application.
 *
 * @author <a href="mailto:sduskis@gmail.com">Solomon Duskis</a>
 * @version $Revision: 1 $
 */

@Service
public class ContactService {
    private final Map<String, Contact> contactMap = new ConcurrentHashMap<>();

    public void save(Contact contact) {
        contactMap.put(contact.getLastName(), contact);
    }

    public Contact getContact(String lastName) {
        return contactMap.get(lastName);
    }

    public Contacts getAll() {
        return new Contacts(contactMap.values());
    }
}
