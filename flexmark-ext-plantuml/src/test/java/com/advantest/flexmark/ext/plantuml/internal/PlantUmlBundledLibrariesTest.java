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
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Test;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import net.sourceforge.plantuml.SourceStringReader;

/**
 * PlantUML's EPL edition brings ELK, EMF, JLaTeXMath and Guava inside its own JAR, and Maven resolves them a
 * second time from the dependencies PlantUML's POM declares, so that licence reports and SBOMs list them. These
 * tests fail if PlantUML stops bundling one of them, or if Maven resolves a version other than the bundled one.
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
	public void plantUmlStillBundlesEachLibrary() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		String plantUmlJar = "jar:" + SourceStringReader.class.getProtectionDomain().getCodeSource().getLocation() + "!/";

		for (String className : CLASSES_OF_BUNDLED_LIBRARIES) {
			List<URL> copies = Collections.list(classLoader.getResources(className));

			assertTrue(className + " found in " + copies,
					copies.stream().anyMatch(copy -> copy.toString().startsWith(plantUmlJar)));
		}
	}

	@Test
	public void eachBundledLibraryResolvesInTheVersionPlantUmlBundles() throws IOException, URISyntaxException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL plantUmlJarLocation = SourceStringReader.class.getProtectionDomain().getCodeSource().getLocation();
		String plantUmlJarUrl = "jar:" + plantUmlJarLocation + "!/";
		List<String> bundled = new ArrayList<>();

		try (JarFile jar = new JarFile(Path.of(plantUmlJarLocation.toURI()).toFile())) {
			jar.stream().map(JarEntry::getName)
					.filter(name -> name.startsWith("META-INF/maven/") && name.endsWith("/pom.properties"))
					.forEach(bundled::add);
		}
		assertFalse(bundled.isEmpty());

		for (String pomProperties : bundled) {
			List<URL> copies = Collections.list(classLoader.getResources(pomProperties));
			List<URL> resolved = copies.stream().filter(copy -> !copy.toString().startsWith(plantUmlJarUrl)).toList();
			URL inPlantUml = copies.stream().filter(copy -> copy.toString().startsWith(plantUmlJarUrl)).findFirst()
					.orElseThrow();

			assertEquals(pomProperties + " found in " + copies, 1, resolved.size());
			assertEquals(pomProperties, versionIn(inPlantUml), versionIn(resolved.get(0)));
		}
	}

	private static String versionIn(URL pomProperties) throws IOException {
		Properties properties = new Properties();
		try (InputStream in = pomProperties.openStream()) {
			properties.load(in);
		}
		return properties.getProperty("version");
	}
}