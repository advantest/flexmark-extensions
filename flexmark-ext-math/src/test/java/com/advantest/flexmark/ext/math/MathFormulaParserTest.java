/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.advantest.flexmark.ext.math.internal.MathFormulaBlockParser;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MathFormulaParserTest {

	private static final DataHolder OPTIONS = new MutableDataSet()
			.set(Parser.EXTENSIONS, Collections.singleton(MathExtension.create()))
			.toImmutable();

	private final Parser parser = Parser.builder(OPTIONS).build();
	private final HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS).build();

	private String render(String markdown) {
		return renderer.render(parser.parse(markdown));
	}

	@Test
	public void spaceAfterOpeningDollarSignIsNoInLineFormula() {
		assertEquals("<p>$ E=mc^2$</p>\n", render("$ E=mc^2$"));
	}

	@Test
	public void letterBeforeOpeningDollarSignIsNoInLineFormula() {
		assertEquals("<p>a$E=mc^2$</p>\n", render("a$E=mc^2$"));
	}

	@Test
	public void spaceBeforeClosingDollarSignIsNoInLineFormula() {
		assertEquals("<p>$E=mc^2 $</p>\n", render("$E=mc^2 $"));
	}

	@Test
	public void letterAfterClosingDollarSignIsNoInLineFormula() {
		assertEquals("<p>$E=mc^2$x</p>\n", render("$E=mc^2$x"));
	}

	@Test
	public void doubleDollarSignsInTextAreNoInLineFormula() {
		assertEquals("<p>text $$x$$ more</p>\n", render("text $$x$$ more"));
	}

	@Test
	public void doubleDollarSignOpeningAndSingleDollarSignClosingAreNoInLineFormula() {
		assertEquals("<p>$$x$ y</p>\n", render("$$x$ y"));
	}

	@Test
	public void doubleDollarSignsNotAtTheStartOfALineAreNoDisplayFormula() {
		Document document = parser.parse("text $$ x\n");

		assertFalse(renderer.render(document).contains("math display"));
	}

	@Test
	public void moreThanTwoDoubleDollarSignsInOneLineAreNoDisplayFormula() {
		Document document = parser.parse("$$ x $$ y $$\n");

		assertFalse(renderer.render(document).contains("math display"));
	}

	@Test
	public void displayFormulaWithoutEndMarkerIsNoDisplayFormula() {
		Document document = parser.parse("$$\nx = 1\n");

		assertFalse(document.getFirstChild() instanceof MathFormulaDisplayModeNode);
		assertFalse(renderer.render(document).contains("math display"));
	}

	@Test
	public void displayFormulaOverSeveralLinesIsRendered() {
		String html = render("$$\nx = 1\ny = 2\n$$\n");

		assertTrue(html.contains("<span class=\"math display\">\\[\nx = 1\ny = 2\n\\]</span>"));
	}

	@Test
	public void textAfterTheClosingMarkerOnTheSameLineIsNoDisplayFormula() {
		assertEquals("<p>$$ x $$ and more</p>\n", render("$$ x $$ and more\n"));
	}

	@Test
	public void textAfterTheClosingMarkerOnItsOwnLineIsNoDisplayFormula() {
		String html = render("$$\nx = 1\n$$ and more\n");

		assertFalse(html.contains("math display"));
		assertTrue(html.contains("and more"));
	}

	@Test
	public void closingMarkerFollowedOnlyBySpaceEndsADisplayFormula() {
		String html = render("$$\nx = 1\n$$  \n");

		assertTrue(html.contains("<span class=\"math display\">\\[\nx = 1\n\\]</span>"));
	}

	@Test
	public void blockParserRequiresBlockData() {
		assertThrows(IllegalArgumentException.class, () -> new MathFormulaBlockParser(null));
	}
}