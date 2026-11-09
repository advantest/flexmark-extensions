/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
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