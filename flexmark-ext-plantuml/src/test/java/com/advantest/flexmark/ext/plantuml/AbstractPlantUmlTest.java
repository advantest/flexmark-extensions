/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.plantuml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

public abstract class AbstractPlantUmlTest {

	private static final DataHolder OPTIONS = new MutableDataSet()
			.set(Parser.EXTENSIONS, Collections.singleton(PlantUmlExtension.create()))
			.set(HtmlRenderer.INDENT_SIZE, 2)
			.set(PlantUmlExtension.KEY_RENDER_FENCED_PLANTUML_CODE_BLOCKS, true)
			.toImmutable();

	protected Parser parser;
	protected HtmlRenderer renderer;

	@Before
	public void setUp() {
		parser = Parser.builder(OPTIONS).build();
		renderer = HtmlRenderer.builder(OPTIONS).build();
	}
	
	@After
	public void cleanUp() {
		parser = null;
		renderer = null;
	}
	
	protected String readFileFromClasspath(String filePath) throws IOException {
		StringBuffer contents = new StringBuffer();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream(filePath)))) {
			
			String line = reader.readLine();
			while (line != null) {
				contents.append(line);
				contents.append("\n");
				line = reader.readLine();
			}
		}
		return contents.toString();
	}
}
