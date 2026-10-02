/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.figures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public class ImageFigureRenderingTest {

	private static MutableDataSet options() {
		return new MutableDataSet()
				.set(Parser.EXTENSIONS, Collections.singleton(FiguresExtension.create()))
				.set(HtmlRenderer.INDENT_SIZE, 2);
	}

	private static String render(MutableDataSet options, String markdown) {
		Document document = Parser.builder(options).build().parse(markdown);
		return HtmlRenderer.builder(options).build().render(document);
	}

	private static String render(String markdown) {
		return render(options(), markdown);
	}

	@Test
	public void titleOfImageIsRenderedAsTitleAttribute() {
		String html = render("![Some image](path/to/file.png \"The title\")");

		assertEquals("<figure>\n"
				+ "  <img src=\"path/to/file.png\" alt=\"Some image\" title=\"The title\" />\n"
				+ "  <figcaption>Some image</figcaption>\n"
				+ "</figure>\n", html);
	}

	@Test
	public void descriptionOfImageIsEscapedInCaption() {
		String html = render("![Tom & Jerry](path/to/file.png)");

		assertTrue(html.contains("alt=\"Tom &amp; Jerry\""));
		assertTrue(html.contains("<figcaption>Tom &amp; Jerry</figcaption>"));
	}

	@Test
	public void imageInHeadingIsRenderedAsFigure() {
		String html = render("# ![Some image](path/to/file.png)");

		assertTrue(html.startsWith("<h1>"));
		assertTrue(html.contains("<figure>"));
		assertTrue(html.contains("<figcaption>Some image</figcaption>"));
		assertTrue(html.trim().endsWith("</h1>"));
	}

	@Test
	public void imageFollowedByTextStaysInlineInItsParagraph() {
		String html = render("![icon](path/to/icon.png) and text");

		assertEquals("<p><img src=\"path/to/icon.png\" alt=\"icon\" /> and text</p>\n", html);
	}

	@Test
	public void imageAfterTextStaysInlineInItsParagraph() {
		String html = render("Text and ![icon](path/to/icon.png)");

		assertEquals("<p>Text and <img src=\"path/to/icon.png\" alt=\"icon\" /></p>\n", html);
	}

	@Test
	public void imagesBetweenTextAreNotRenderedIfLinksAreNotRendered() {
		MutableDataSet options = options().set(HtmlRenderer.DO_NOT_RENDER_LINKS, true);

		String html = render(options, "Text ![icon](path/to/icon.png) and more");

		assertEquals("<p>Text  and more</p>\n", html);
	}

	@Test
	public void imagesWithSuppressedLinkPrefixAreNotRenderedBetweenText() {
		String html = render("Text ![icon](javascript:alert(1)) and more");

		assertEquals("<p>Text  and more</p>\n", html);
	}

	@Test
	public void imageWithSuppressedLinkPrefixIsRenderedIfNothingIsSuppressed() {
		MutableDataSet options = options().set(HtmlRenderer.SUPPRESSED_LINKS, "");

		String html = render(options, "Text ![icon](javascript:alert(1)) and more");

		assertTrue(html.contains("<img src=\"javascript:alert(1)\" alt=\"icon\" />"));
	}

	@Test
	public void contentFollowingTheUrlOfAnImageIsAppendedToTheSource() {
		MutableDataSet options = options();
		Document document = Parser.builder(options).build().parse("Text ![icon](path/to/icon.png) and more");
		Image image = (Image) document.getFirstChild().getChildOfType(Image.class);
		assertNotNull(image);
		image.setUrlContent(BasedSequence.of("?a=b&c=d"));

		String html = HtmlRenderer.builder(options).build().render(document);

		assertTrue(html.contains("src=\"path/to/icon.png?a=b&amp;c=d\""));
		assertFalse(html.contains("&amp;amp;"));
	}
}