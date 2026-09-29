/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml;

/**
 * Answers where the PlantUML files a document refers to were looked for.
 * 
 * <p>Rendering a document never reads a referenced file itself, so it also never learns where that
 * file would have been. Whoever hands over the PlantUML code of a document's references answers
 * this question as well, and a diagram that could not be rendered can then say where its file was
 * expected instead of only that it is missing.</p>
 * 
 * <p>The question is only ever asked about a reference that produced no code, so nothing is
 * resolved for a document that renders without a failure.</p>
 * 
 * @see PlantUmlExtension#KEY_DOCUMENT_PLANTUML_FILE_LOCATIONS
 */
@FunctionalInterface
public interface PlantUmlFileLocations {

	/**
	 * Answers where the given reference was looked for.
	 * 
	 * @param target the reference as it is written in the document, e.g.
	 *               <code>diagrams/classes.puml</code>, never <code>null</code>
	 * @return where that reference was looked for, or <code>null</code> if this side cannot say it
	 */
	PlantUmlFileLocation locationOf(String target);

}
