/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.figures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class FiguresExtensionTest {

	private static final String MARKDOWN = "![Some image](path/to/file.png)\n";

	private static String render(MutableDataSet options) {
		Document document = Parser.builder(options).build().parse(MARKDOWN);
		return HtmlRenderer.builder(options).build().render(document);
	}

	@Test
	public void imageIsRenderedAsFigureForHtml() {
		MutableDataSet options = new MutableDataSet()
				.set(Parser.EXTENSIONS, Collections.singleton(FiguresExtension.create()));

		assertTrue(render(options).startsWith("<figure>"));
	}

	@Test
	public void imageIsNotRenderedAsFigureForOtherRendererTypes() {
		MutableDataSet options = new MutableDataSet()
				.set(Parser.EXTENSIONS, Collections.singleton(FiguresExtension.create()))
				.set(HtmlRenderer.TYPE, "JIRA");

		String html = render(options);

		assertFalse(html.contains("<figure>"));
		assertTrue(html.contains("<img src=\"path/to/file.png\" alt=\"Some image\" />"));
	}
}