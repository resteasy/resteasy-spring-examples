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

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MyWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // `ResteasyBootstrap` is not mandatory if you want to setup `ResteasyContext` and `ResteasyDeployment` manually.
        servletContext.addListener(ResteasyBootstrap.class);

        // Create Spring context.
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(MyConfig.class);

        // Implement our own `ContextLoaderListener`, so we can implement our own `SpringBeanProcessor` if necessary.
        servletContext.addListener(new MyContextLoaderListener(context));
    }
}
