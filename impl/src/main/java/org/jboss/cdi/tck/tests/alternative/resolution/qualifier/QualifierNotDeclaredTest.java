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

package org.jboss.cdi.tck.tests.alternative.resolution.qualifier;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class QualifierNotDeclaredTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(QualifierNotDeclaredTest.class)
                .withClasses(Foo.class, Bar.class, Baz.class, Qux.class, True.class, TrueLiteral.class)
                .build();
    }

    @Inject
    private Qux qux;

    /**
     * {@link Baz} implements {@link Foo} and has {@link True} qualifier declared. {@link Bar} is enabled alternative and
     * implements {@link Foo} but it has not {@link True} declared. Therefore the result of typesafe resolution for type
     * {@link Foo} and qualifier {@link True} is {@link Baz} bean.
     *
     * @throws Exception
     */
    @Test
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "ba")
    @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "lb")
    public void testResolution() throws Exception {

        Bean<?> bean = getCurrentManager().resolve(getCurrentManager().getBeans(Foo.class, TrueLiteral.INSTANCE));
        assertEquals(bean.getBeanClass(), Baz.class);

        assertNotNull(qux);
        assertEquals(qux.getFoo().ping(), 1);
    }

}
