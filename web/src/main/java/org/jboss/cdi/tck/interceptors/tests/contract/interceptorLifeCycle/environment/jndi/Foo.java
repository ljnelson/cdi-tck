/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.environment.jndi;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.Dependent;

import javax.naming.InitialContext;
import javax.naming.NamingException;

@MyBinding
@Dependent
public class Foo {

    @Resource(name = "greeting")
    private String greeting;

    private Animal animal;

    public String getGreeting() {
        return greeting;
    }

    public Animal getAnimal() {
        return animal;
    }

    @PostConstruct
    private void init() {
        try {
            this.animal = (Animal) InitialContext.doLookup("java:module/Animal");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
