/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.jira.tickets.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.advantest.flexmark.ext.jira.tickets.JiraTicketExtension;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class JiraTicketNumbersOptionsTest {

	@Test
	public void defaultUrlIsUsedIfNoneIsConfigured() {
		JiraTicketNumbersOptions options = new JiraTicketNumbersOptions(new MutableDataSet());

		assertEquals(JiraTicketExtension.JIRA_URL.getDefaultValue(new MutableDataSet()), options.jiraTicketUrl);
	}

	@Test
	public void configuredUrlIsUsed() {
		MutableDataSet source = new MutableDataSet().set(JiraTicketExtension.JIRA_URL, "https://jira.example.com/");

		assertEquals("https://jira.example.com/", new JiraTicketNumbersOptions(source).jiraTicketUrl);
	}

	@Test
	public void optionsAreWrittenToOtherDataHolder() {
		MutableDataSet source = new MutableDataSet().set(JiraTicketExtension.JIRA_URL, "https://jira.example.com/");
		MutableDataSet target = new MutableDataSet();

		new JiraTicketNumbersOptions(source).setIn(target);

		assertEquals("https://jira.example.com/", JiraTicketExtension.JIRA_URL.get(target));
	}
}