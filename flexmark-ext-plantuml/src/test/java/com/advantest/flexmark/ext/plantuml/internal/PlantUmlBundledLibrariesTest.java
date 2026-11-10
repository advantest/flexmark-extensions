/*
 * Copyright © 2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.plantuml.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import net.sourceforge.plantuml.SourceStringReader;

/**
 * PlantUML's EPL edition brings ELK, JLaTeXMath and Guava inside its own JAR, and the build excludes the
 * dependencies its POM declares for them. These tests fail if PlantUML stops bundling one of them, so that it
 * would be missing, or if a second copy of one of them reaches the classpath.
 */
public class PlantUmlBundledLibrariesTest {

	private static final String[] CLASSES_OF_BUNDLED_LIBRARIES = {
			"org/eclipse/elk/core/RecursiveGraphLayoutEngine.class",
			"org/eclipse/elk/alg/layered/LayeredLayoutProvider.class",
			"org/eclipse/elk/graph/ElkNode.class",
			"org/eclipse/emf/ecore/EObject.class",
			"org/scilab/forge/jlatexmath/TeXFormula.class",
			"org/scilab/forge/jlatexmath/greek/GreekRegistration.class",
			"org/scilab/forge/jlatexmath/cyrillic/CyrillicRegistration.class",
			"com/google/common/base/Preconditions.class",
			"com/google/common/util/concurrent/internal/InternalFutureFailureAccess.class" };

	@Test
	public void classDiagramIsLaidOutWithElk() {
		String svg = new PlantUmlBlockNodeRenderer().translatePlantUmlToSvg("""
				@startuml
				!pragma layout elk
				class Cat
				class Dog
				Cat --> Dog
				@enduml
				""");

		assertTrue(svg, svg.startsWith("<svg"));
		assertTrue(svg, svg.contains("Cat"));
		assertTrue(svg, svg.contains("Dog"));
		assertFalse(svg, svg.contains("not supported"));
		assertFalse(svg, svg.contains("Exception"));
	}

	@Test
	public void latexFormulaIsRenderedWithJLaTeXMath() {
		BufferedImage image = (BufferedImage) new TeXFormula("\\alpha + \\sum_{i=0}^{n} x_i^2")
				.createBufferedImage(TeXConstants.STYLE_DISPLAY, 20, Color.BLACK, Color.WHITE);

		assertTrue(image.getWidth() > 0);
		assertTrue(image.getHeight() > 0);
	}

	@Test
	public void eachBundledLibraryIsOnTheClasspathOnlyOnceAndComesFromPlantUml() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL plantUmlJar = SourceStringReader.class.getProtectionDomain().getCodeSource().getLocation();

		for (String className : CLASSES_OF_BUNDLED_LIBRARIES) {
			List<URL> copies = Collections.list(classLoader.getResources(className));

			assertEquals(className + " found in " + copies, 1, copies.size());
			assertTrue(className + " found in " + copies.get(0),
					copies.get(0).toString().startsWith("jar:" + plantUmlJar + "!/"));
		}
	}
}
