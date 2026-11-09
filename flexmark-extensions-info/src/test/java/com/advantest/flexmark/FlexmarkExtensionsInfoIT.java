/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import org.junit.Test;

/**
 * Reads the version from the JAR the build made, which the integration tests run on rather than on
 * the class folder.
 */
public class FlexmarkExtensionsInfoIT {

    private static String expectedVersion() {
        String expected = System.getProperty("expectedVersion");
        assertNotNull("The build names the version it expects.", expected);
        return expected;
    }

    @Test
    public void readsTheVersionFromTheJar() {
        URL resource = FlexmarkExtensionsInfo.class.getResource("/" + FlexmarkExtensionsInfo.VERSION_RESOURCE);
        assertNotNull(resource);
        assertEquals("jar", resource.getProtocol());

        assertEquals(expectedVersion(), FlexmarkExtensionsInfo.getVersion());
    }

    @Test
    public void namesTheVersionInTheManifestOfTheJar() throws Exception {
        File jarFile = new File(FlexmarkExtensionsInfo.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        try (JarFile jar = new JarFile(jarFile)) {
            assertEquals(expectedVersion(),
                    jar.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION));
        }
    }
}