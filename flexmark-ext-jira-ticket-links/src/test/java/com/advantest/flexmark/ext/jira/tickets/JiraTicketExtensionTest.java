/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.jira.tickets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class JiraTicketExtensionTest {

	private static MutableDataSet options() {
		return new MutableDataSet()
				.set(Parser.EXTENSIONS, Collections.singleton(JiraTicketExtension.create()))
				.set(JiraTicketExtension.JIRA_URL, "https://jira.example.com/browse/");
	}

	private static String render(MutableDataSet options, String markdown) {
		return HtmlRenderer.builder(options).build().render(Parser.builder(options).build().parse(markdown));
	}

	@Test
	public void ticketNumberBecomesLinkForHtml() {
		String html = render(options(), "See ABC-123 now");

		assertEquals("<p>See <a href=\"https://jira.example.com/browse/ABC-123\">ABC-123</a> now</p>\n", html);
	}

	@Test
	public void ticketNumberIsNotLinkedForOtherRendererTypes() {
		MutableDataSet options = options().set(HtmlRenderer.TYPE, "JIRA");

		String html = render(options, "See ABC-123 now");

		assertTrue(!html.contains("<a "));
	}

	@Test
	public void ticketNumberIsRenderedAsPlainTextIfLinksAreNotRendered() {
		MutableDataSet options = options().set(HtmlRenderer.DO_NOT_RENDER_LINKS, true);

		String html = render(options, "See ABC-123 now");

		assertEquals("<p>See ABC-123 now</p>\n", html);
	}

	@Test
	public void ticketNumberStartingTheTextIsLinked() {
		String html = render(options(), "ABC-1");

		assertEquals("<p><a href=\"https://jira.example.com/browse/ABC-1\">ABC-1</a></p>\n", html);
	}

	@Test
	public void ticketNumberDirectlyAfterLetterIsNotLinked() {
		String html = render(options(), "xABC-1");

		assertTrue(!html.contains("<a "));
	}

	@Test
	public void ticketNumberDirectlyAfterUnderscoreIsNotLinked() {
		String html = render(options(), "_ABC-1");

		assertTrue(!html.contains("<a "));
	}

	@Test
	public void customProjectKeyPatternIsUsed() {
		MutableDataSet options = options().set(JiraTicketExtension.JIRA_PROJECT_KEY_REGEX, "HMR");

		String html = render(options, "HMR-5 and ABC-6");

		assertTrue(html.contains(">HMR-5</a>"));
		assertTrue(!html.contains(">ABC-6</a>"));
	}
}