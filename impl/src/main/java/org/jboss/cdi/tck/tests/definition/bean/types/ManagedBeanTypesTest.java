/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.definition.bean.types;

import static org.jboss.cdi.tck.cdi.Sections.MANAGED_BEAN_TYPES;
import static org.jboss.cdi.tck.util.Assert.assertTypeSetMatches;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ManagedBeanTypesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ManagedBeanTypesTest.class).build();
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_TYPES, id = "a") })
    public void testGenericHierarchyBeanTypes() {

        // Generic class inheritance with abstact class and interface
        Bean<GriffonVulture> vultureBean = getUniqueBean(GriffonVulture.class);
        assertNotNull(vultureBean);
        // Object, Animal<Integer>, Bird<String, Integer>, Vulture<Integer>, GriffonVulture
        assertEquals(vultureBean.getTypes().size(), 5);
        assertTypeSetMatches(vultureBean.getTypes(), Object.class, GriffonVulture.class, new TypeLiteral<Animal<Integer>>() {
        }.getType(), new TypeLiteral<Bird<String, Integer>>() {
        }.getType(), new TypeLiteral<Vulture<Integer>>() {
        }.getType());

        // Generic class inheritance with two interfaces
        Bean<Tiger> tigerBean = getUniqueBean(Tiger.class);
        assertNotNull(tigerBean);
        // Object, Animal<String>, Mammal<String>, Tiger
        assertEquals(tigerBean.getTypes().size(), 4);
        assertTypeSetMatches(tigerBean.getTypes(), Object.class, Tiger.class, new TypeLiteral<Animal<String>>() {
        }.getType(), new TypeLiteral<Mammal<String>>() {
        }.getType());

        // Nested generic class inheritance
        Bean<Flock> flockBean = getUniqueBean(Flock.class);
        assertNotNull(flockBean);
        // Object, Flock, Gathering<Vulture<Integer>>, GroupingOfCertainType<Vulture<Integer>>
        assertTypeSetMatches(flockBean.getTypes(), Object.class, Flock.class, new TypeLiteral<Gathering<Vulture<Integer>>>() {
        }.getType(), new TypeLiteral<GroupingOfCertainType<Vulture<Integer>>>() {
        }.getType());
    }

}
