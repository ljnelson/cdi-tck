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
package org.jboss.cdi.tck.tests.full.lookup.injectionpoint;

import java.util.Date;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

@Decorator
public class TimestampLogger implements Logger {
    @Inject
    @Delegate
    private Logger logger;

    private static Logger staticLogger;

    private static String loggedMessage;

    public void log(String message) {
        staticLogger = logger;
        loggedMessage = message;
        logger.log(new Date().toString() + ":  " + message);
    }

    public static Logger getLogger() {
        return staticLogger;
    }

    /**
     * @return the loggedMessage
     */
    public static String getLoggedMessage() {
        return loggedMessage;
    }

    public static void reset() {
        loggedMessage = null;
        staticLogger = null;
    }

}
