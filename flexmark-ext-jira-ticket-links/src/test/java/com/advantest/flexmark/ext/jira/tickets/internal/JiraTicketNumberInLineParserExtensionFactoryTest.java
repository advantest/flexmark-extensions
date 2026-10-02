/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.jira.tickets.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JiraTicketNumberInLineParserExtensionFactoryTest {

	private final JiraTicketNumberInLineParserExtension.Factory factory =
			new JiraTicketNumberInLineParserExtension.Factory();

	@Test
	public void factoryIsTriggeredByUpperCaseLettersAndDigits() {
		String characters = factory.getCharacters().toString();

		assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", characters);
	}

	@Test
	public void factoryDeclaresNoOrderingDependencies() {
		assertNull(factory.getAfterDependents());
		assertNull(factory.getBeforeDependents());
	}

	@Test
	public void factoryDoesNotAffectGlobalScope() {
		assertFalse(factory.affectsGlobalScope());
	}
}