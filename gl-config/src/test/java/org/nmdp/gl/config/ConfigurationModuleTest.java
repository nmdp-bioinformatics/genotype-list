/*

    gl-config  Property and environment variable configuration module.
    Copyright (c) 2012-2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/
package org.nmdp.gl.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;

import org.junit.Test;

/**
 * Unit test for ConfigurationModule.
 */
public final class ConfigurationModuleTest {

    /**
     * Module with one bound string property.
     */
    final static class PropertyModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindProperty("org.immunogenomics.gl.config.Property").toValue("value");
        }
    }

    /**
     * Module with one bound string property read from a classpath resource property file.
     */
    final static class PropertyFileModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindProperties("/org/nmdp/gl/config/property.properties");
        }
    }

    /**
     * Module with one bound integer property.
     */
    final static class IntegerPropertyModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindProperty("org.immunogenomics.gl.config.IntegerProperty").toValue("42");
        }
    }

    /**
     * Module with one bound integer property read from a classpath resource property file.
     */
    final static class IntegerPropertyFileModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindProperties("/org/nmdp/gl/config/integer-property.properties");
        }
    }

    /**
     * Module with multiple inner class bound properties.
     */
    final static class OuterInnerModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindProperty("org.immunogenomics.gl.config.Outer$Inner1").toValue("value1");
            bindProperty("org.immunogenomics.gl.config.Outer$Inner2").toValue("value2");
        }
    }

    /**
     * Module with multiple inner class bound properties read from a classpath resource property file.
     */
    final static class OuterInnerFileModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindProperties("/org/nmdp/gl/config/outer-inner.properties");
        }
    }

    /**
     * Module with only named properties read from a classpath resource property file.
     */
    final static class NamedFileModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindProperties("/org/nmdp/gl/config/named.properties");
        }
    }

    /**
     * Module calling bindPropertiesAndEnvironmentVariables with null classpath resource property file.
     */
    final static class NullPropertyFileModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindPropertiesWithOverrides(null);
        }
    }

    /**
     * Module with one bound string property read from a classpath resource property file
     * which may be overridden by system properties or environment variables.
     */
    final static class PropertyFileWithOverridesModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindPropertiesWithOverrides("/org/nmdp/gl/config/property.properties");
        }
    }

    /**
     * Target module.
     */
    final static class TargetModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Target.class).in(Singleton.class);
        }
    }

    @Test
    public void testPropertyModule() {
        Injector injector = Guice.createInjector(new PropertyModule());
        assertNotNull(injector);

        Binding<String> binding = injector.getBinding(Key.get(String.class, Property.class));
        assertNotNull(binding);
        String value = binding.getProvider().get();
        assertEquals("value", value);
    }

    @Test
    public void testPropertyFileModule() {
        Injector injector = Guice.createInjector(new PropertyFileModule());
        assertNotNull(injector);

        Binding<String> binding = injector.getBinding(Key.get(String.class, Property.class));
        assertNotNull(binding);
        String value = binding.getProvider().get();
        assertEquals("value", value);
    }

    @Test
    public void testIntegerPropertyModule() {
        Injector injector = Guice.createInjector(new IntegerPropertyModule());
        assertNotNull(injector);

        Binding<Integer> binding = injector.getBinding(Key.get(Integer.class, IntegerProperty.class));
        assertNotNull(binding);
        Integer value = binding.getProvider().get();
        assertEquals(Integer.valueOf(42), value);
    }

    @Test
    public void testIntegerPropertyFileModule() {
        Injector injector = Guice.createInjector(new IntegerPropertyFileModule());
        assertNotNull(injector);

        Binding<Integer> binding = injector.getBinding(Key.get(Integer.class, IntegerProperty.class));
        assertNotNull(binding);
        Integer value = binding.getProvider().get();
        assertEquals(Integer.valueOf(42), value);
    }

    @Test
    public void testOuterInnerModule() {
        Injector injector = Guice.createInjector(new OuterInnerModule());
        assertNotNull(injector);

        Binding<String> binding1 = injector.getBinding(Key.get(String.class, Outer.Inner1.class));
        assertNotNull(binding1);
        String value1 = binding1.getProvider().get();
        assertEquals("value1", value1);

        Binding<String> binding2 = injector.getBinding(Key.get(String.class, Outer.Inner2.class));
        assertNotNull(binding2);
        String value2 = binding2.getProvider().get();
        assertEquals("value2", value2);
    }

    @Test
    public void testOuterInnerFileModule() {
        Injector injector = Guice.createInjector(new OuterInnerModule());
        assertNotNull(injector);

        Binding<String> binding1 = injector.getBinding(Key.get(String.class, Outer.Inner1.class));
        assertNotNull(binding1);
        String value1 = binding1.getProvider().get();
        assertEquals("value1", value1);

        Binding<String> binding2 = injector.getBinding(Key.get(String.class, Outer.Inner2.class));
        assertNotNull(binding2);
        String value2 = binding2.getProvider().get();
        assertEquals("value2", value2);
    }

    @Test
    public void testPropertyTarget() {
        Injector injector = Guice.createInjector(new PropertyFileModule(), new IntegerPropertyFileModule(), new OuterInnerFileModule(), new NamedFileModule(), new TargetModule());
        assertNotNull(injector);

        Target target = injector.getInstance(Target.class);
        assertEquals("value", target.getValue());
        assertEquals(42, target.getIntegerValue());
        assertEquals("value1", target.getInner1Value());
        assertEquals("value2", target.getInner2Value());
        assertEquals("namedValue", target.getNamedValue());
    }

    @Test(expected=com.google.inject.CreationException.class)
    public void testBindPropertiesWithOverridesNullPropertyFile() {
        // turn off Guice's Logger
        Logger.getLogger("com.google.inject").setLevel(Level.OFF);

        Guice.createInjector(new NullPropertyFileModule());
    }

    @Test
    public void testBindPropertiesWithOverrides() {
        Injector injector = Guice.createInjector(new PropertyFileWithOverridesModule());
        assertNotNull(injector);

        Binding<String> binding = injector.getBinding(Key.get(String.class, Property.class));
        assertNotNull(binding);
        String value = binding.getProvider().get();
        assertEquals("value", value);
    }
}