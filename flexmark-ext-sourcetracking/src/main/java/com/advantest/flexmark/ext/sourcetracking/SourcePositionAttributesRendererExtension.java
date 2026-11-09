/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.sourcetracking;

import org.jetbrains.annotations.NotNull;

import com.advantest.flexmark.ext.sourcetracking.internal.SourcePositionAttributesProvider;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.HtmlRenderer.Builder;
import com.vladsch.flexmark.util.data.MutableDataHolder;

/**
 * flexmark-java extension adding source code position details to the rendered HTML elements.
 * This enables navigation from Markdown source code to the rendered element in an HTML preview and vice versa.
 */
public class SourcePositionAttributesRendererExtension implements HtmlRenderer.HtmlRendererExtension {
	
	public static final String SOURCE_OFFSET_ATTRIBUTE_NAME = "source-offset";
	public static final String SOURCE_LENGTH_ATTRIBUTE_NAME = "source-length";

	private SourcePositionAttributesRendererExtension() {
	}

	public static SourcePositionAttributesRendererExtension create() {
		return new SourcePositionAttributesRendererExtension();
	}

	@Override
	public void rendererOptions(@NotNull MutableDataHolder options) {
		// no changes
	}

	@Override
	public void extend(@NotNull Builder htmlRendererBuilder, @NotNull String rendererType) {
		htmlRendererBuilder.attributeProviderFactory(SourcePositionAttributesProvider.factory());
	}

}
