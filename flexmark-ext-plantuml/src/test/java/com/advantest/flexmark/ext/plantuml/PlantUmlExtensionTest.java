/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class PlantUmlExtensionTest {

	private static final String FENCED_CODE = """
			```plantuml
			@startuml
			class ArrayList
			@enduml
			```
			""";

	private static final String BLOCK_CODE = """
			@startuml
			class ArrayList
			@enduml
			""";

	private static String render(DataHolder options, String markdown) {
		Document document = Parser.builder(options).build().parse(markdown);
		return HtmlRenderer.builder(options).build().render(document);
	}

	private static MutableDataSet optionsWithExtension() {
		return new MutableDataSet().set(Parser.EXTENSIONS, Collections.singleton(PlantUmlExtension.create()));
	}

	@Test
	public void fencedPlantUmlCodeBlockIsNotRenderedAsDiagramByDefault() {
		String html = render(optionsWithExtension().toImmutable(), FENCED_CODE);

		assertFalse(html.contains("<svg"));
		assertTrue(html.startsWith("<pre><code class=\"language-plantuml\">@startuml"));
	}

	@Test
	public void fencedPlantUmlCodeBlockIsNotRenderedAsDiagramIfSwitchedOff() {
		MutableDataSet options = optionsWithExtension()
				.set(PlantUmlExtension.KEY_RENDER_FENCED_PLANTUML_CODE_BLOCKS, false);

		String html = render(options.toImmutable(), FENCED_CODE);

		assertFalse(html.contains("<svg"));
		assertTrue(html.startsWith("<pre><code class=\"language-plantuml\">@startuml"));
	}

	@Test
	public void fencedPlantUmlCodeBlockIsRenderedAsDiagramIfSwitchedOn() {
		MutableDataSet options = optionsWithExtension()
				.set(PlantUmlExtension.KEY_RENDER_FENCED_PLANTUML_CODE_BLOCKS, true);

		String html = render(options.toImmutable(), FENCED_CODE);

		assertTrue(html.startsWith("<figure>"));
		assertTrue(html.contains("<svg"));
	}

	@Test
	public void plantUmlCodeBlockIsRenderedAsDiagramForHtml() {
		String html = render(optionsWithExtension().toImmutable(), BLOCK_CODE);

		assertTrue(html.startsWith("<figure>"));
		assertTrue(html.contains("<svg"));
	}

	@Test
	public void plantUmlCodeBlockIsNotRenderedAsDiagramForOtherRendererTypes() {
		MutableDataSet options = optionsWithExtension().set(HtmlRenderer.TYPE, "JIRA");

		String html = render(options.toImmutable(), BLOCK_CODE);

		assertFalse(html.contains("<svg"));
		assertFalse(html.contains("<figure>"));
	}
}