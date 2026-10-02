/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.math;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MathExtensionTest {

	private static final String MARKDOWN = "A formula $E=mc^2$ in text.\n\n$$\nx = 1\n$$\n";

	private static MutableDataSet optionsWithExtension() {
		return new MutableDataSet().set(Parser.EXTENSIONS, Collections.singleton(MathExtension.create()));
	}

	private static String render(MutableDataSet options) {
		Document document = Parser.builder(options).build().parse(MARKDOWN);
		return HtmlRenderer.builder(options).build().render(document);
	}

	@Test
	public void formulasAreRenderedForHtml() {
		String html = render(optionsWithExtension());

		assertTrue(html.contains("math inline"));
		assertTrue(html.contains("math display"));
	}

	@Test
	public void formulasAreNotRenderedForOtherRendererTypes() {
		String html = render(optionsWithExtension().set(HtmlRenderer.TYPE, "JIRA"));

		assertFalse(html.contains("math inline"));
		assertFalse(html.contains("math display"));
	}
}