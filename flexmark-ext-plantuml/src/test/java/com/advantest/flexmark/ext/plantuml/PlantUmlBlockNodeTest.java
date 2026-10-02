/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.vladsch.flexmark.util.ast.BlockContent;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class PlantUmlBlockNodeTest extends AbstractPlantUmlTest {

	private static final String MARKDOWN = """
			@startuml
			class ArrayList
			ArrayList ..|> List
			@enduml
			""";

	private PlantUmlBlockNode parseBlock() {
		Document document = parser.parse(MARKDOWN);
		Node block = document.getFirstChild();
		assertTrue(block instanceof PlantUmlBlockNode);
		return (PlantUmlBlockNode) block;
	}

	@Test
	public void parsedBlockKnowsItsStartMarker() {
		assertEquals("@startuml", parseBlock().getStartMarker().toString());
	}

	@Test
	public void parsedBlockKnowsItsEndMarker() {
		assertEquals("@enduml", parseBlock().getEndMarker().toString());
	}

	@Test
	public void parsedBlockContentLeavesOutTheMarkerLines() {
		String content = parseBlock().getContentChars().toString();

		assertEquals("class ArrayList\nArrayList ..|> List\n", content);
	}

	@Test
	public void segmentsOfParsedBlockAreStartMarkerContentAndEndMarker() {
		PlantUmlBlockNode block = parseBlock();

		BasedSequence[] segments = block.getSegments();

		assertEquals(3, segments.length);
		assertSame(block.getStartMarker(), segments[0]);
		assertEquals(block.getContentChars().toString(), segments[1].toString());
		assertSame(block.getEndMarker(), segments[2]);
	}

	@Test
	public void markersOfNewBlockAreEmpty() {
		PlantUmlBlockNode block = new PlantUmlBlockNode();

		assertSame(BasedSequence.NULL, block.getStartMarker());
		assertSame(BasedSequence.NULL, block.getEndMarker());
	}

	@Test
	public void markersCanBeChanged() {
		PlantUmlBlockNode block = new PlantUmlBlockNode();
		BasedSequence start = BasedSequence.of("@startuml");
		BasedSequence end = BasedSequence.of("@enduml");

		block.setStartMarker(start);
		block.setEndMarker(end);

		assertSame(start, block.getStartMarker());
		assertSame(end, block.getEndMarker());
	}

	@Test
	public void blockCreatedFromCharsHasTheseChars() {
		PlantUmlBlockNode block = new PlantUmlBlockNode(BasedSequence.of("class A"));

		assertEquals("class A", block.getChars().toString());
	}

	@Test
	public void blockCreatedFromLinesSpansAllOfThem() {
		List<BasedSequence> lines = BasedSequence.of("class A\nclass B\n").splitListEOL();

		PlantUmlBlockNode block = new PlantUmlBlockNode(lines);

		assertEquals("class A\nclass B\n", block.getChars().toString());
		assertEquals("class A\nclass B\n", block.getContentChars().toString());
	}

	@Test
	public void blockCreatedFromNoLinesHasNoChars() {
		PlantUmlBlockNode block = new PlantUmlBlockNode(List.<BasedSequence>of());

		assertEquals(0, block.getChars().length());
	}

	@Test
	public void blockCreatedFromCharsAndLinesKeepsBoth() {
		BasedSequence chars = BasedSequence.of("class A\nclass B\n");

		PlantUmlBlockNode block = new PlantUmlBlockNode(chars, chars.splitListEOL());

		assertEquals(chars.toString(), block.getChars().toString());
		assertEquals(2, block.getContentLines().size());
	}

	@Test
	public void blockCreatedFromEmptyBlockContentHasNoContent() {
		PlantUmlBlockNode block = new PlantUmlBlockNode(new BlockContent());

		assertNotNull(block.getContentChars());
		assertEquals(0, block.getContentChars().length());
	}
}