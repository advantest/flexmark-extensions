/*
 * Copyright © 2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.plantuml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.vladsch.flexmark.util.ast.Document;

import net.sourceforge.plantuml.error.PSystemError;

/**
 * PlantUML decorates its error diagrams depending on the minute of the hour, so the same erroneous diagram would render
 * differently from one minute to the next. These tests switch the decorations on again before each test and check that
 * rendering switches them off before drawing.
 */
public class PlantUmlErrorDiagramTest extends AbstractPlantUmlTest {

	private static final String ERRONEOUS_DIAGRAM = "@startuml\nclass ArrayList {\n@enduml\n";

	private static void setTimeBasedErrorDecorationsDisabled(boolean disabled) throws ReflectiveOperationException {
		decorationsFlag().setBoolean(null, disabled);
	}

	private static boolean timeBasedErrorDecorationsDisabled() throws ReflectiveOperationException {
		return decorationsFlag().getBoolean(null);
	}

	private static Field decorationsFlag() throws NoSuchFieldException {
		Field flag = PSystemError.class.getDeclaredField("disableTimeBasedErrorDecorations");
		flag.setAccessible(true);
		return flag;
	}

	private static void assertStableErrorDiagram(String html) throws ReflectiveOperationException {
		assertTrue(timeBasedErrorDecorationsDisabled());
		assertTrue(html, html.contains("<svg"));
		assertTrue(html, html.contains("Syntax Error"));
		String lowerCase = html.toLowerCase(Locale.ROOT);
		assertFalse(lowerCase.contains("patreon"));
		assertFalse(lowerCase.contains("liberapay"));
	}

	@Before
	public void enableTimeBasedErrorDecorations() throws ReflectiveOperationException {
		setTimeBasedErrorDecorationsDisabled(false);
	}

	@Test
	public void entryPointDisablesTimeBasedErrorDecorations() throws ReflectiveOperationException {
		PlantUmlExtension.disableTimeBasedErrorDecorations();
		PlantUmlExtension.disableTimeBasedErrorDecorations();

		assertTrue(timeBasedErrorDecorationsDisabled());
	}

	@Test
	public void erroneousCodeBlockRendersWithoutTimeBasedDecorations() throws ReflectiveOperationException {
		String html = renderer.render(parser.parse(ERRONEOUS_DIAGRAM));

		assertStableErrorDiagram(html);
	}

	@Test
	public void erroneousFencedCodeBlockRendersWithoutTimeBasedDecorations() throws ReflectiveOperationException {
		String html = renderer.render(parser.parse("```plantuml\n" + ERRONEOUS_DIAGRAM + "```\n"));

		assertStableErrorDiagram(html);
	}

	@Test
	public void erroneousPumlFileRendersWithoutTimeBasedDecorations() throws ReflectiveOperationException {
		Document document = parser.parse("![diagram](diagrams/broken.puml)");
		document.set(PlantUmlExtension.KEY_DOCUMENT_PATH_TO_FILE_CONTENTS_MAP,
				Map.of("diagrams/broken.puml", ERRONEOUS_DIAGRAM));

		String html = renderer.render(document);

		assertStableErrorDiagram(html);
	}

	@Test
	public void erroneousDiagramRendersTheSameEachTime() throws ReflectiveOperationException {
		String first = renderer.render(parser.parse(ERRONEOUS_DIAGRAM));
		setTimeBasedErrorDecorationsDisabled(false);
		String second = renderer.render(parser.parse(ERRONEOUS_DIAGRAM));

		assertEquals(first, second);
	}
}
