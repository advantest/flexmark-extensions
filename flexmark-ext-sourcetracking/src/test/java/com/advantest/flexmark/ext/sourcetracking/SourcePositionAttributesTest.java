/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.sourcetracking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class SourcePositionAttributesTest {

	private static final DataHolder OPTIONS = new MutableDataSet()
			.set(Parser.EXTENSIONS, Collections.singleton(SourcePositionAttributesRendererExtension.create()))
			.toImmutable();

	private static final String HEADING_MARKDOWN = "# Heading";
	private static final String LINK_URL = "https://example.com";
	private static final String LINK_MARKDOWN = "[link](" + LINK_URL + ")";
	private static final String PARAGRAPH_MARKDOWN = "Some text with a " + LINK_MARKDOWN + ".";
	private static final String MARKDOWN = HEADING_MARKDOWN + "\n\n" + PARAGRAPH_MARKDOWN + "\n";

	@Test
	public void sourcePositionAttributesAreRendered() {
		String html = normalizeWhitespace(render(MARKDOWN));

		assertNotNull(html);

		int headingOffset = MARKDOWN.indexOf(HEADING_MARKDOWN);
		String heading = "<h1 " + sourceAttributes(headingOffset, HEADING_MARKDOWN.length()) + ">Heading</h1>";
		assertTrue(html, html.contains(heading));

		int paragraphOffset = MARKDOWN.indexOf(PARAGRAPH_MARKDOWN);
		// The paragraph is the last block, so its node spans from its start to the end of the
		// document (its trailing line break is part of the paragraph node).
		int paragraphLength = MARKDOWN.length() - paragraphOffset;
		String paragraph = "<p " + sourceAttributes(paragraphOffset, paragraphLength) + ">";
		assertTrue(html, html.contains(paragraph));

		int linkOffset = MARKDOWN.indexOf(LINK_MARKDOWN);
		String link = "<a href=\"" + LINK_URL + "\" " + sourceAttributes(linkOffset, LINK_MARKDOWN.length())
				+ ">link</a>";
		assertTrue(html, html.contains(link));
	}

	@Test
	public void noSourcePositionAttributesWithoutExtension() {
		String markdown = "# Heading\n";

		Parser parser = Parser.builder().build();
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String html = renderer.render(parser.parse(markdown));

		assertEquals("<h1>Heading</h1>\n", html);
	}

	private static String sourceAttributes(int offset, int length) {
		return SourcePositionAttributesRendererExtension.SOURCE_OFFSET_ATTRIBUTE_NAME
				+ "=\"" + offset + "\" "
				+ SourcePositionAttributesRendererExtension.SOURCE_LENGTH_ATTRIBUTE_NAME
				+ "=\"" + length + "\"";
	}

	private static String normalizeWhitespace(String html) {
		return html.replaceAll("\\s+", " ");
	}

	private String render(String markdown) {
		Parser parser = Parser.builder(OPTIONS).build();
		HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS).build();
		Document document = parser.parse(markdown);
		return renderer.render(document);
	}
}
