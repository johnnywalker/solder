/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.solder.test.bean.generic.alternative;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.solder.test.util.MavenArtifactResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class GenericBeanAlternativeTest {
    @Deployment(name = "GenericBeanAlternative")
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
            .addAsLibraries(
                    MavenArtifactResolver.resolve("org.jboss.solder", "solder-api"),
                    MavenArtifactResolver.resolve("org.jboss.solder", "solder-impl"),
                    MavenArtifactResolver.resolve("org.jboss.solder", "solder-logging"))        
                .addPackage(GenericBeanAlternativeTest.class.getPackage())
                .addAsWebInfResource("org/jboss/solder/test/bean/generic/alternative/beans.xml", "beans.xml");
    }

    @Inject
    @Big
    Pow bigPow;

    @Inject
    @Small
    Pow smallPow;

    @Inject
    @Big
    Bop bigBop;

    @Inject
    @Small
    Bop smallBop;

    @Test
    public void testGenericAlternatives() {
        Assert.assertEquals("Alternative Big Bam", bigPow.getName());
        Assert.assertEquals("Small Bam", smallPow.getName());
    }

    @Test
    public void testGenericProducerMethodAlternatives() {
        Assert.assertEquals("Alternative Big Bam", bigBop.getName());
        Assert.assertEquals("Small Bam", smallBop.getName());
    }

}