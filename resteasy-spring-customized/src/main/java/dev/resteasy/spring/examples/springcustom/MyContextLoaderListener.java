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

import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.plugins.spring.i18n.Messages;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

public class MyContextLoaderListener extends ContextLoaderListener {
    public MyContextLoaderListener(WebApplicationContext context) {
        super(context);
    }

    @Override
    protected void customizeContext(ServletContext servletContext,
            ConfigurableWebApplicationContext configurableWebApplicationContext) {
        super.customizeContext(servletContext, configurableWebApplicationContext);

        ResteasyDeployment deployment = (ResteasyDeployment) servletContext.getAttribute(ResteasyDeployment.class.getName());
        if (deployment == null) {
            throw new RuntimeException(Messages.MESSAGES.deploymentIsNull());
        }

        SpringBeanProcessor processor = new SpringBeanProcessor(deployment);
        configurableWebApplicationContext.addBeanFactoryPostProcessor(processor);
        configurableWebApplicationContext.addApplicationListener(processor);
    }
}
