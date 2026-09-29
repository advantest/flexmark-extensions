/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml;

/**
 * Where a PlantUML file referenced by a document was looked for, and whether something was there.
 * 
 * <p>A document names a diagram the way its author wrote it down, e.g.
 * <code>diagrams/classes.puml</code>, which says nothing about where that file was searched. Only
 * whoever resolved the reference knows that, and only that side can say it in a form the reader of
 * the document can act on: an absolute path on the machine the document lives on, a URL of a
 * repository's web interface, or whatever else names the place a reader may open.</p>
 * 
 * @param resolvedPath the place the file was looked for, as the resolving side names it towards a
 *                     human reader, never <code>null</code>
 * @param exists whether something was there at the moment it was looked for &ndash; a file that is
 *               there but could not be read is a different failure from one that is missing, and a
 *               reader is told which of the two happened
 */
public record PlantUmlFileLocation(String resolvedPath, boolean exists) {

	/**
	 * Creates the location of a referenced PlantUML file.
	 * 
	 * @throws IllegalArgumentException if the given path is <code>null</code>
	 */
	public PlantUmlFileLocation {
		if (resolvedPath == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
	}

}
