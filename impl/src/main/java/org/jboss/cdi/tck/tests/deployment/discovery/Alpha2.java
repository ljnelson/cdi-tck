package org.jboss.cdi.tck.tests.deployment.discovery;

import jakarta.enterprise.context.Dependent;

@Dependent
public class Alpha2 implements Ping {

    @Override
    public void pong() {
    }
}
