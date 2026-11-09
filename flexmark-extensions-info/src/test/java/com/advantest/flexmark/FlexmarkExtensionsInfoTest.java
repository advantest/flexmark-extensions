/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FlexmarkExtensionsInfoTest {

    @Test
    public void readsTheVersionOfTheBuild() {
        String expected = System.getProperty("expectedVersion");
        assertNotNull("The build names the version it expects.", expected);

        assertEquals(expected, FlexmarkExtensionsInfo.getVersion());
        assertFalse(FlexmarkExtensionsInfo.getVersion().contains("${"));
    }

    @Test
    public void answersNoVersionWhereTheResourceIsMissing() {
        assertNull(FlexmarkExtensionsInfo.readVersion("META-INF/com.advantest.flexmark/no-such-module/version.properties"));
    }

    @Test
    public void answersNoVersionWhereTheResourceNamesNone() {
        assertNull(FlexmarkExtensionsInfo.readVersion("com/advantest/flexmark/no-version.properties"));
    }

    @Test
    public void answersNoVersionWhereTheResourceWasNotFilledIn() {
        assertNull(FlexmarkExtensionsInfo.readVersion("com/advantest/flexmark/unfiltered-version.properties"));
    }
}