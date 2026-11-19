/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.plantuml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class PlantUmlFencedCodeBlockTest extends AbstractPlantUmlTest {

	@Test
	public void simpleFencedPlantUmlCodeBlockRendered() {
		final String markdownCode = """
				```plantuml
				@startuml
				  interface SomeInterface
				  SomeInterface <|.. SomeClass
				@enduml
				```
				""";
		final String expectedRegex = """
				<figure>
				  <svg [^<>]+>[\\s\\S]+
				  </svg>
				</figure>
				""";
		
		Document document = parser.parse(markdownCode);
		String resultHtml = renderer.render(document);
		
		assertNotNull(resultHtml);
		assertTrue(resultHtml.matches(expectedRegex));
	}
	
	@Test
	public void simpleFencedPlantUmlCodeBlockWithEmptyLineFollowingRendered() {
		final String markdownCode = """
				```plantuml
				@startuml
				  interface SomeInterface
				  SomeInterface <|.. SomeClass
				@enduml
				```
				
				""";
		final String expectedRegex = """
				<figure>
				  <svg [^<>]+>[\\s\\S]+
				  </svg>
				</figure>
				""";
		
		Document document = parser.parse(markdownCode);
		String resultHtml = renderer.render(document);
		
		assertNotNull(resultHtml);
		assertTrue(resultHtml.matches(expectedRegex));
	}
	
	@Test
	public void simpleFencedPlantUmlCodeBlockAlternativeSyntaxRendered() {
		final String markdownCode = """
				~~~plantuml
				@startuml
				  interface SomeInterface
				  SomeInterface <|.. SomeClass
				@enduml
				~~~
				""";
		final String expectedRegex = """
				<figure>
				  <svg [^<>]+>[\\s\\S]+
				  </svg>
				</figure>
				""";
		
		Document document = parser.parse(markdownCode);
		String resultHtml = renderer.render(document);
		
		assertNotNull(resultHtml);
		assertTrue(resultHtml.matches(expectedRegex));
	}
	
	@Test
	public void fencedNonPlantUmlCodeBlockRendered() {
		final String markdownCode = """
				```
				@startuml
				  interface SomeInterface
				  SomeInterface <|.. SomeClass
				@enduml
				```
				""";
		final String expected = """
				<pre><code>@startuml
				  interface SomeInterface
				  SomeInterface &lt;|.. SomeClass
				@enduml
				</code></pre>
				""";
		
		Document document = parser.parse(markdownCode);
		String resultHtml = renderer.render(document);
		
		assertNotNull(resultHtml);
		assertEquals(resultHtml, expected);
	}
	
	@Test
	public void fencedNonPlantUmlCodeBlockWithLanguageRendered() {
		final String markdownCode = """
				```plantumlcode
				@startuml
				  interface SomeInterface
				  SomeInterface <|.. SomeClass
				@enduml
				```
				""";
		final String expected = """
				<pre><code class="language-plantumlcode">@startuml
				  interface SomeInterface
				  SomeInterface &lt;|.. SomeClass
				@enduml
				</code></pre>
				""";
		
		Document document = parser.parse(markdownCode);
		String resultHtml = renderer.render(document);
		
		assertNotNull(resultHtml);
		assertEquals(resultHtml, expected);
	}

	private static final String DIAGRAM_MARKDOWN = """
			```plantuml
			@startuml
			  interface SomeInterface
			  SomeInterface <|.. SomeClass
			@enduml
			```
			""";

	private static final String LIGHT_BACKGROUND = "background: #FFFFFF;";
	private static final String DARK_BACKGROUND = "background: #1B1B1B;";

	@Test
	public void fencedPlantUmlCodeBlockIsRenderedLightByDefault() {
		String resultHtml = renderer.render(parser.parse(DIAGRAM_MARKDOWN));

		assertTrue(resultHtml.contains(LIGHT_BACKGROUND));
		assertFalse(resultHtml.contains(DARK_BACKGROUND));
	}

	@Test
	public void fencedPlantUmlCodeBlockIsRenderedDarkWhenTheOptionSaysSo() {
		DataHolder options = new MutableDataSet()
				.set(Parser.EXTENSIONS, Collections.singleton(PlantUmlExtension.create()))
				.set(PlantUmlExtension.KEY_RENDER_FENCED_PLANTUML_CODE_BLOCKS, true)
				.set(PlantUmlExtension.KEY_DARK_MODE, true)
				.toImmutable();

		String resultHtml = HtmlRenderer.builder(options).build()
				.render(Parser.builder(options).build().parse(DIAGRAM_MARKDOWN));

		assertTrue(resultHtml.contains(DARK_BACKGROUND));
		assertFalse(resultHtml.contains(LIGHT_BACKGROUND));
	}

	@Test
	public void darkModeOfTheDocumentOverridesTheOptionsForThatDocumentOnly() {
		Document darkDocument = parser.parse(DIAGRAM_MARKDOWN);
		darkDocument.set(PlantUmlExtension.KEY_DARK_MODE, true);
		Document lightDocument = parser.parse(DIAGRAM_MARKDOWN);

		String darkHtml = renderer.render(darkDocument);
		String lightHtml = renderer.render(lightDocument);

		assertTrue(darkHtml.contains(DARK_BACKGROUND));
		assertTrue(lightHtml.contains(LIGHT_BACKGROUND));
		assertNotEquals(darkHtml, lightHtml);
	}

	@Test
	public void oneRendererServesLightAndDarkDocumentsConcurrently() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		try {
			List<Future<Boolean>> results = new ArrayList<>();
			for (int i = 0; i < 8; i++) {
				boolean dark = i % 2 == 0;
				results.add(executor.submit(() -> {
					Document document = parser.parse(DIAGRAM_MARKDOWN);
					document.set(PlantUmlExtension.KEY_DARK_MODE, dark);
					String html = renderer.render(document);
					return html.contains(dark ? DARK_BACKGROUND : LIGHT_BACKGROUND)
							&& !html.contains(dark ? LIGHT_BACKGROUND : DARK_BACKGROUND);
				}));
			}
			for (Future<Boolean> result : results) {
				assertTrue(result.get(2, TimeUnit.MINUTES));
			}
		} finally {
			executor.shutdownNow();
		}
	}
}
