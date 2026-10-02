/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class MathFormulaInLineNodeTest {

	private final Parser parser = Parser.builder(new MutableDataSet()
			.set(Parser.EXTENSIONS, Collections.singleton(MathExtension.create()))).build();

	@Test
	public void parsedFormulaKnowsMarkersAndText() {
		Document document = parser.parse("$E=mc^2$");

		Node formula = document.getFirstChild().getFirstChild();

		assertTrue(formula instanceof MathFormulaInLineNode);
		MathFormulaInLineNode node = (MathFormulaInLineNode) formula;
		assertEquals("$", node.getOpeningMarker().toString());
		assertEquals("E=mc^2", node.getText().toString());
		assertEquals("$", node.getClosingMarker().toString());
	}

	@Test
	public void segmentsAreOpeningMarkerTextAndClosingMarker() {
		BasedSequence source = BasedSequence.of("$E=mc^2$");
		MathFormulaInLineNode node = new MathFormulaInLineNode(source.subSequence(0, 1),
				source.subSequence(1, 7), source.subSequence(7, 8));

		BasedSequence[] segments = node.getSegments();

		assertEquals(3, segments.length);
		assertEquals("$", segments[0].toString());
		assertEquals("E=mc^2", segments[1].toString());
		assertEquals("$", segments[2].toString());
	}

	@Test
	public void formulaCreatedFromItsPartsSpansFromOpeningToClosingMarker() {
		BasedSequence source = BasedSequence.of("$E=mc^2$");

		MathFormulaInLineNode node = new MathFormulaInLineNode(source.subSequence(0, 1),
				source.subSequence(1, 7), source.subSequence(7, 8));

		assertEquals("$E=mc^2$", node.getChars().toString());
	}

	@Test
	public void formulaCreatedFromCharsHasTheseChars() {
		MathFormulaInLineNode node = new MathFormulaInLineNode(BasedSequence.of("$E=mc^2$"));

		assertEquals("$E=mc^2$", node.getChars().toString());
	}

	@Test
	public void markersAndTextOfNewFormulaAreEmpty() {
		MathFormulaInLineNode node = new MathFormulaInLineNode();

		assertSame(BasedSequence.NULL, node.getOpeningMarker());
		assertSame(BasedSequence.NULL, node.getText());
		assertSame(BasedSequence.NULL, node.getClosingMarker());
	}

	@Test
	public void markersAndTextCanBeChanged() {
		MathFormulaInLineNode node = new MathFormulaInLineNode();
		BasedSequence opening = BasedSequence.of("$");
		BasedSequence text = BasedSequence.of("x");
		BasedSequence closing = BasedSequence.of("$");

		node.setOpeningMarker(opening);
		node.setText(text);
		node.setClosingMarker(closing);

		assertSame(opening, node.getOpeningMarker());
		assertSame(text, node.getText());
		assertSame(closing, node.getClosingMarker());
	}
}