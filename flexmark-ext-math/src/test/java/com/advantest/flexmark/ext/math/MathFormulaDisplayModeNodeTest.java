/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.BlockContent;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class MathFormulaDisplayModeNodeTest {

	private final Parser parser = Parser.builder(new MutableDataSet()
			.set(Parser.EXTENSIONS, Collections.singleton(MathExtension.create()))).build();

	@Test
	public void parsedFormulaKnowsItsStartAndEndMarker() {
		Document document = parser.parse("$$\nE=mc^2\n$$\n");

		Node formula = document.getFirstChild();

		assertTrue(formula instanceof MathFormulaDisplayModeNode);
		MathFormulaDisplayModeNode node = (MathFormulaDisplayModeNode) formula;
		assertEquals("$$", node.getStartMarker().toString());
		assertEquals("$$", node.getEndMarker().toString());
	}

	@Test
	public void markersOfNewFormulaAreNotSet() {
		MathFormulaDisplayModeNode node = new MathFormulaDisplayModeNode();

		assertNull(node.getStartMarker());
		assertNull(node.getEndMarker());
	}

	@Test
	public void markersCanBeChanged() {
		MathFormulaDisplayModeNode node = new MathFormulaDisplayModeNode();
		BasedSequence marker = BasedSequence.of("$$");

		node.setStartMarker(marker);
		node.setEndMarker(marker);

		assertSame(marker, node.getStartMarker());
		assertSame(marker, node.getEndMarker());
	}

	@Test
	public void formulaCreatedFromCharsHasTheseChars() {
		MathFormulaDisplayModeNode node = new MathFormulaDisplayModeNode(BasedSequence.of("$$x$$"));

		assertEquals("$$x$$", node.getChars().toString());
	}

	@Test
	public void formulaCreatedFromLinesSpansAllOfThem() {
		List<BasedSequence> lines = BasedSequence.of("x = 1\ny = 2\n").splitListEOL();

		MathFormulaDisplayModeNode node = new MathFormulaDisplayModeNode(lines);

		assertEquals("x = 1\ny = 2\n", node.getChars().toString());
		assertEquals(2, node.getContentLines().size());
	}

	@Test
	public void formulaCreatedFromNoLinesHasNoChars() {
		MathFormulaDisplayModeNode node = new MathFormulaDisplayModeNode(List.<BasedSequence>of());

		assertEquals(0, node.getChars().length());
	}

	@Test
	public void formulaCreatedFromCharsAndLinesKeepsBoth() {
		BasedSequence chars = BasedSequence.of("x = 1\ny = 2\n");

		MathFormulaDisplayModeNode node = new MathFormulaDisplayModeNode(chars, chars.splitListEOL());

		assertEquals(chars.toString(), node.getChars().toString());
		assertEquals(2, node.getContentLines().size());
	}

	@Test
	public void formulaCreatedFromEmptyBlockContentHasNoContent() {
		MathFormulaDisplayModeNode node = new MathFormulaDisplayModeNode(new BlockContent());

		assertNotNull(node.getContentChars());
		assertEquals(0, node.getContentChars().length());
	}
}