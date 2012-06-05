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
package org.jboss.cdi.tck.tests.lookup.dynamic.builtin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for built-in Instance.
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class BuiltinInstanceTest extends AbstractTest {

    @SuppressWarnings("serial")
    private static final AnnotationLiteral<FarmBased> farmBasedLiteral = new AnnotationLiteral<FarmBased>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BuiltinInstanceTest.class).build();
    }

    @Test
    @SpecAssertion(section = "5.6.2", id = "d")
    public void testScopeOfBuiltinInstance() {
        @SuppressWarnings("serial")
        Bean<Instance<Cow>> bean = getUniqueBean(new TypeLiteral<Instance<Cow>>() {
        });
        assertEquals(bean.getScope(), Dependent.class);
    }

    @Test
    @SpecAssertion(section = "5.6.2", id = "e")
    public void testNameOfBuiltinInstance() {
        @SuppressWarnings("serial")
        Bean<Instance<Cow>> bean = getUniqueBean(new TypeLiteral<Instance<Cow>>() {
        });
        assertNull(bean.getName());
    }

    @SuppressWarnings({ "serial", "rawtypes" })
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    // CDI-232
    // @SpecAssertion(section = "5.6.2", id = "a")
    @SpecAssertions({ @SpecAssertion(section = "5.6.2", id = "f") })
    public void testInstanceProvidedForEveryLegalBeanType(Farm farm, Instance<Predator<?>> predatorInstance) {

        // Interface
        Bean<?> instanceBean = getUniqueBean(new TypeLiteral<Instance<Animal>>() {
        });
        // Abstract class
        assertEquals(getUniqueBean(new TypeLiteral<Instance<AbstractAnimal>>() {
        }), instanceBean);
        // Final class
        assertEquals(getUniqueBean(new TypeLiteral<Instance<FinalAnimal>>() {
        }), instanceBean);
        // Parameterized and raw type
        assertEquals(getUniqueBean(new TypeLiteral<Instance<Wolf>>() {
        }), instanceBean);
        assertEquals(getUniqueBean(new TypeLiteral<Instance<Predator>>() {
        }), instanceBean);
        // Array
        assertEquals(getUniqueBean(new TypeLiteral<Instance<Sheep[]>>() {
        }), instanceBean);
        // Primitive
        assertEquals(getUniqueBean(new TypeLiteral<Instance<Integer>>() {
        }, farmBasedLiteral), instanceBean);

        assertNotNull(predatorInstance);
        predatorInstance.select(Wolf.class).get().attack(null);

        assertNotNull(farm.getAnimal());
        assertNotNull(farm.getAbstractAnimal());
        assertNotNull(farm.getCow());
        farm.getCow().get().ping();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "5.6.2", id = "g")
    public void testInstanceIsPassivationCapable(Field field) throws Exception {

        assertNotNull(field);

        Object object = deserialize(serialize(field));
        assert field.getInstance().get() instanceof Cow;
        assert object instanceof Field;
        Field field2 = (Field) object;
        assert field2.getInstance().get() instanceof Cow;
    }
}
