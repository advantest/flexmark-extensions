/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2022-2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;

public class PlantUmlImageTest extends AbstractPlantUmlTest {

	private static final String REGEX_IMAGES_RENDERED = """
			<h1>Heading</h1>
			<p>Some text
			here and
			there\\.</p>
			<p><img src=\\"path/to/file\\.png\\" alt=\\"Some image\\" /></p>
			<p>Some more text with <img src=\\"folder/file\\.jpg\\" alt=\\"icon\\" /> and image in-lined\\.</p>
			<figure>
			  <svg [^<>]+>[\\s\\S]+
			  </svg>
			  <figcaption>PlantUML diagram</figcaption>
			</figure>
			<p>Follow-up text\\.</p>
			""";

	@Test
	public void referencedPumlFilesRenderedToSvg() throws IOException {
		URL resource = this.getClass().getResource("/images.md");
		String mdFileContent = readFileFromClasspath("/images.md");

		Document document = parser.parse(mdFileContent);
		document.set(PlantUmlExtension.KEY_DOCUMENT_FILE_PATH, resource.getPath());
		Map<String, String> referencedFileContents = new HashMap<>();
		referencedFileContents.put("diagrams/classes.puml", readFileFromClasspath("/diagrams/classes.puml"));
		document.set(PlantUmlExtension.KEY_DOCUMENT_PATH_TO_FILE_CONTENTS_MAP, referencedFileContents);

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		assertTrue(resultHtml.matches(REGEX_IMAGES_RENDERED));
	}

	@Test
	public void renderErrorMessageForMissingPumlFile() {
		URL resource = this.getClass().getResource("/images.md");
		String mdFileContent = "![label](path/to/missing/file.puml)";

		Document document = parser.parse(mdFileContent);
		document.set(PlantUmlExtension.KEY_DOCUMENT_FILE_PATH, resource.getPath());

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		String expectedPrefix = "<span style=\"color:red\">PlantUML file";
		String expectedSuffix = "does not exist.</span>\n";
		assertEquals(expectedPrefix, resultHtml.substring(0, expectedPrefix.length()));
		assertEquals(expectedSuffix, resultHtml.substring(resultHtml.length() - expectedSuffix.length()));
	}

	private static final String PUML_CODE = "@startuml\nclass ArrayList\n@enduml\n";

	private String renderWithDiagram(String markdown) {
		Document document = parser.parse(markdown);
		document.set(PlantUmlExtension.KEY_DOCUMENT_PATH_TO_FILE_CONTENTS_MAP,
				Map.of("diagrams/classes.puml", PUML_CODE));
		return renderer.render(document);
	}

	@Test
	public void imageWithoutLabelIsRenderedWithoutCaption() {
		String resultHtml = renderWithDiagram("![](diagrams/classes.puml)");

		assertTrue(resultHtml.contains("<svg"));
		assertFalse(resultHtml.contains("figcaption"));
	}

	@Test
	public void labelOfImageIsEscapedInCaption() {
		String resultHtml = renderWithDiagram("![Tom & Jerry](diagrams/classes.puml)");

		assertTrue(resultHtml.contains("<figcaption>Tom &amp; Jerry</figcaption>"));
	}

	@Test
	public void imageBetweenTextStaysInsideItsParagraph() {
		String resultHtml = renderWithDiagram("Before ![diagram](diagrams/classes.puml) after.");

		assertTrue(resultHtml.startsWith("<p>Before "));
		assertTrue(resultHtml.contains("<figure>"));
		assertTrue(resultHtml.trim().endsWith("after.</p>"));
	}

	@Test
	public void imageAfterTextStaysInsideItsParagraph() {
		String resultHtml = renderWithDiagram("Before ![diagram](diagrams/classes.puml)");

		assertTrue(resultHtml.startsWith("<p>Before "));
		assertTrue(resultHtml.contains("<svg"));
		assertTrue(resultHtml.trim().endsWith("</p>"));
	}

	@Test
	public void imageBeforeTextStaysInsideItsParagraph() {
		String resultHtml = renderWithDiagram("![diagram](diagrams/classes.puml) after.");

		assertTrue(resultHtml.startsWith("<p>"));
		assertTrue(resultHtml.contains("<figure>"));
		assertTrue(resultHtml.trim().endsWith("after.</p>"));
	}

	@Test
	public void imageInHeadingIsRenderedInsideTheHeading() {
		String resultHtml = renderWithDiagram("# ![diagram](diagrams/classes.puml)");

		assertTrue(resultHtml.startsWith("<h1>"));
		assertTrue(resultHtml.contains("<svg"));
		assertTrue(resultHtml.trim().endsWith("</h1>"));
	}

	@Test
	public void imageOfOtherFileTypeIsNotReplaced() {
		Document document = parser.parse("![diagram](diagrams/classes.png)");

		assertNull(findPlantUmlImage(document));
		assertFalse(renderer.render(document).contains("<figure>"));
	}

	@Test
	public void plantUmlImageKeepsTheParts() {
		Document document = parser.parse("![the label](diagrams/classes.puml \"the title\")");

		PlantUmlImage image = findPlantUmlImage(document);

		assertEquals("the label", image.getText().toString());
		assertEquals("diagrams/classes.puml", image.getUrl().toString());
		assertEquals("the title", image.getTitle().toString());
	}

	@Test
	public void plantUmlImageReplacesTheImageNode() {
		Document document = parser.parse("![the label](diagrams/classes.puml)");

		assertNotNull(findPlantUmlImage(document));
		assertNull(findImage(document, false));
	}

	@Test
	public void newPlantUmlImageHasNoParts() {
		PlantUmlImage image = new PlantUmlImage();

		assertEquals(0, image.getUrl().length());
		assertEquals(0, image.getText().length());
	}

	private PlantUmlImage findPlantUmlImage(Document document) {
		return (PlantUmlImage) findImage(document, true);
	}

	private Image findImage(Document document, boolean plantUml) {
		for (Node node : document.getDescendants()) {
			if (node instanceof Image image && (image instanceof PlantUmlImage) == plantUml) {
				return image;
			}
		}
		return null;
	}

}