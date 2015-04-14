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

import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;

import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit test for ConfigurationModule which uses PowerMock to mock system properties and environment variables.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class})
public final class ConfigurationModuleOverridesTest {
    private Map<String, String> environmentVariables = ImmutableMap.of("org_nmdp_gl_config_Property", "overridden-value");

    /**
     * Module with one bound string property read from a classpath resource property file
     * which may be overridden by environment variables.
     */
    final static class PropertyFileWithOverridesModule extends ConfigurationModule {
        @Override
        protected void bindConfigurations() {
            bindPropertiesWithOverrides("/org/nmdp/gl/config/property.properties");
        }
    }

    /*

      Not sure why these don't work.  System is properly mocked in this test class
      but the calls made by ConfigurationModule through Guice are not.

    @Test
    public void testBindPropertiesWithOverridesOverriddenByEnvironmentVariable() {
        mockStatic(System.class);
        when(System.getenv()).thenReturn(environmentVariables);
        assertEquals(environmentVariables, System.getenv());

        Injector injector = Guice.createInjector(new PropertyFileWithOverridesModule());
        assertNotNull(injector);

        Binding<String> binding = injector.getBinding(Key.get(String.class, Property.class));
        assertNotNull(binding);
        String value = binding.getProvider().get();
        assertEquals("overridden-value", value);
    }

    @Test
    public void testBindPropertiesWithOverridesOverriddenBySystemProperty() {
        mockStatic(System.class);

        Properties systemProperties = new Properties();
        systemProperties.putAll(environmentVariables);

        when(System.getProperties()).thenReturn(systemProperties);
        assertEquals(systemProperties, System.getProperties());

        Injector injector = Guice.createInjector(new PropertyFileWithOverridesModule());
        assertNotNull(injector);

        Binding<String> binding = injector.getBinding(Key.get(String.class, Property.class));
        assertNotNull(binding);
        String value = binding.getProvider().get();
        assertEquals("overridden-value", value);
    }
    */
}
