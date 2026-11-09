/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.jira.tickets;

import org.jetbrains.annotations.NotNull;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.sequence.BasedSequence;

/**
 * Node representing Jira ticket numbers like HMR-157.
 */
public class JiraTicketNumberNode extends Node {

	protected BasedSequence text = BasedSequence.NULL;

	public JiraTicketNumberNode() {
	}

	public JiraTicketNumberNode(BasedSequence chars) {
		super(chars);
	}

	@Override
	public @NotNull BasedSequence[] getSegments() {
		return EMPTY_SEGMENTS;
	}

}
