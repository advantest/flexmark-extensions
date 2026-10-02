/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.jira.tickets;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vladsch.flexmark.util.sequence.BasedSequence;

public class JiraTicketNumberNodeTest {

	@Test
	public void nodeKeepsTheTextItWasCreatedFor() {
		JiraTicketNumberNode node = new JiraTicketNumberNode(BasedSequence.of("ABC-123"));

		assertEquals("ABC-123", node.getChars().toString());
	}

	@Test
	public void nodeCreatedWithoutTextIsEmpty() {
		JiraTicketNumberNode node = new JiraTicketNumberNode();

		assertEquals(0, node.getChars().length());
	}

	@Test
	public void nodeHasNoSegments() {
		JiraTicketNumberNode node = new JiraTicketNumberNode(BasedSequence.of("ABC-123"));

		assertEquals(0, node.getSegments().length);
	}
}