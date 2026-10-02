/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class PlantUmlFencedCodeBlockNodeTest extends AbstractPlantUmlTest {

	private static final String MARKDOWN = """
			```plantuml
			@startuml
			class ArrayList
			@enduml
			```
			""";

	private PlantUmlFencedCodeBlockNode parseBlock() {
		Document document = parser.parse(MARKDOWN);
		Node block = document.getFirstChild();
		assertTrue(block instanceof PlantUmlFencedCodeBlockNode);
		return (PlantUmlFencedCodeBlockNode) block;
	}

	@Test
	public void fencedBlockWithPlantUmlInfoIsReplacedByPlantUmlNode() {
		Document document = parser.parse(MARKDOWN);

		assertTrue(document.getFirstChild() instanceof PlantUmlFencedCodeBlockNode);
		assertNull(document.getFirstChild().getNext());
	}

	@Test
	public void replacingNodeKeepsInfoAndMarkersAndContentOfTheFencedBlock() {
		PlantUmlFencedCodeBlockNode block = parseBlock();

		assertEquals("plantuml", block.getInfo().toString());
		assertEquals("```", block.getOpeningMarker().toString());
		assertEquals("```", block.getClosingMarker().toString());
		assertEquals("@startuml\nclass ArrayList\n@enduml\n", block.getContentChars().toString());
	}

	@Test
	public void replacingNodeKeepsTheAttributesOfTheFencedBlock() {
		FencedCodeBlock original = new FencedCodeBlock();
		original.setInfo(BasedSequence.of("plantuml"));
		original.setAttributes(BasedSequence.of("{#id}"));

		PlantUmlFencedCodeBlockNode copy = new PlantUmlFencedCodeBlockNode(original);

		assertEquals("plantuml", copy.getInfo().toString());
		assertEquals("{#id}", copy.getAttributes().toString());
	}

	@Test
	public void markersOfNewNodeAreNotSet() {
		PlantUmlFencedCodeBlockNode block = new PlantUmlFencedCodeBlockNode();

		assertNull(block.getStartMarker());
		assertNull(block.getEndMarker());
	}

	@Test
	public void markersCanBeChanged() {
		PlantUmlFencedCodeBlockNode block = new PlantUmlFencedCodeBlockNode();
		BasedSequence start = BasedSequence.of("@startuml");
		BasedSequence end = BasedSequence.of("@enduml");

		block.setStartMarker(start);
		block.setEndMarker(end);

		assertSame(start, block.getStartMarker());
		assertSame(end, block.getEndMarker());
	}

	@Test
	public void nodeCreatedFromCharsHasTheseChars() {
		PlantUmlFencedCodeBlockNode block = new PlantUmlFencedCodeBlockNode(BasedSequence.of("```plantuml"));

		assertEquals("```plantuml", block.getChars().toString());
	}

	@Test
	public void nodeCreatedFromAllPartsKeepsThem() {
		BasedSequence chars = BasedSequence.of("```plantuml\nclass A\n```");
		BasedSequence opening = chars.subSequence(0, 3);
		BasedSequence info = chars.subSequence(3, 11);
		BasedSequence closing = chars.subSequence(20);

		PlantUmlFencedCodeBlockNode block = new PlantUmlFencedCodeBlockNode(chars, opening, info,
				chars.subSequence(12, 20).splitListEOL(), closing);

		assertEquals("```", block.getOpeningMarker().toString());
		assertEquals("plantuml", block.getInfo().toString());
		assertEquals("```", block.getClosingMarker().toString());
		assertEquals("class A\n", block.getContentChars().toString());
	}
}