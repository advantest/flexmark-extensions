/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class PlantUmlFileLocationTest {

	@Test
	public void locationKnowsItsPathAndWhetherSomethingIsThere() {
		PlantUmlFileLocation location = new PlantUmlFileLocation("/docs/classes.puml", true);

		assertEquals("/docs/classes.puml", location.resolvedPath());
		assertEquals(true, location.exists());
	}

	@Test
	public void locationWithoutPathIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> new PlantUmlFileLocation(null, false));
	}
}