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

package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.interceptor;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorProcessBeanAttributesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(InterceptorProcessBeanAttributesTest.class)
                .withClasses(VerifyingExtension.class, AlphaInterceptor.class, AlphaInterceptorBinding.class)
                .withExtension(VerifyingExtension.class)
                .withBeanLibrary(BravoInterceptor.class, BravoInterceptorBinding.class)
                .withBeanLibrary(new BeansXml(BeanDiscoveryMode.ALL).interceptors(AlphaInterceptor.class, BravoInterceptor.class))
                .withBeansXml(new BeansXml().interceptors(AlphaInterceptor.class, BravoInterceptor.class))
                .build();
    }

    @Test
    @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "ab")
    public void testAlphaInterceptorObserved() {
        assertEquals(VerifyingExtension.aplhaInterceptorObserved.get(), 1);
    }

    @Test
    @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "ab")
    public void testBravoInterceptorObserved() {
        assertEquals(VerifyingExtension.bravoInterceptorObserved.get(), 1);
    }
}
