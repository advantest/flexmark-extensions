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
		String mdFileContent = readFileFromClasspath("/images.md");

		Document document = parser.parse(mdFileContent);
		Map<String, String> referencedFileContents = new HashMap<>();
		referencedFileContents.put("diagrams/classes.puml", readFileFromClasspath("/diagrams/classes.puml"));
		document.set(PlantUmlExtension.KEY_DOCUMENT_PATH_TO_FILE_CONTENTS_MAP, referencedFileContents);

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		assertTrue(resultHtml.matches(REGEX_IMAGES_RENDERED));
	}

	@Test
	public void renderErrorMessageForPumlFileNobodyHandedOver() {
		String mdFileContent = "![label](path/to/missing/file.puml)";

		Document document = parser.parse(mdFileContent);

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		assertEquals("<span style=\"color:red\">No PlantUML code available for"
				+ " &quot;path/to/missing/file.puml&quot;.</span>\n", resultHtml);
	}

	@Test
	public void renderErrorMessageForPumlFileMissingInTheMapOfContents() {
		String mdFileContent = "![label](path/to/missing/file.puml)";

		Document document = parser.parse(mdFileContent);
		document.set(PlantUmlExtension.KEY_DOCUMENT_PATH_TO_FILE_CONTENTS_MAP, new HashMap<>());

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		assertEquals("<span style=\"color:red\">No PlantUML code available for"
				+ " &quot;path/to/missing/file.puml&quot;.</span>\n", resultHtml);
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

	@Test
	public void renderErrorMessageSayingWherePumlFileWasLookedFor() {
		String mdFileContent = "![label](path/to/missing/file.puml)";

		Document document = parser.parse(mdFileContent);
		document.set(PlantUmlExtension.KEY_DOCUMENT_PLANTUML_FILE_LOCATIONS,
				target -> new PlantUmlFileLocation("/home/reader/docs/" + target, false));

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		assertEquals("<span style=\"color:red\">PlantUML file &quot;path/to/missing/file.puml&quot;"
				+ " (resolved path: &quot;/home/reader/docs/path/to/missing/file.puml&quot;) does"
				+ " not exist.</span>\n", resultHtml);
	}

	@Test
	public void renderErrorMessageForPumlFileThatIsThereButWasNotRead() {
		String mdFileContent = "![label](path/to/unreadable/file.puml)";

		Document document = parser.parse(mdFileContent);
		document.set(PlantUmlExtension.KEY_DOCUMENT_PLANTUML_FILE_LOCATIONS,
				target -> new PlantUmlFileLocation("/home/reader/docs/" + target, true));

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		assertEquals("<span style=\"color:red\">Could not read PlantUML file"
				+ " &quot;path/to/unreadable/file.puml&quot;</span>\n", resultHtml);
	}

	@Test
	public void renderErrorMessageWhereNobodySaysWherePumlFileWasLookedFor() {
		String mdFileContent = "![label](path/to/missing/file.puml)";

		Document document = parser.parse(mdFileContent);
		document.set(PlantUmlExtension.KEY_DOCUMENT_PLANTUML_FILE_LOCATIONS, target -> null);

		String resultHtml = renderer.render(document);

		assertNotNull(resultHtml);
		assertEquals("<span style=\"color:red\">No PlantUML code available for"
				+ " &quot;path/to/missing/file.puml&quot;.</span>\n", resultHtml);
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
