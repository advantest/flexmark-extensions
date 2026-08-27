/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2026 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.sourcetracking.internal;

import com.advantest.flexmark.ext.sourcetracking.SourcePositionAttributesRendererExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.CoreNodeRenderer;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.html.MutableAttributes;

/**
 * Attribute provider for flexmark for adding source code position details to rendered HTML elements
 * for enabling navigation from source code to the rendered element in HTML / Markdown preview and vice versa.
 */
public class SourcePositionAttributesProvider implements AttributeProvider {

	@Override
	public void setAttributes(Node node, AttributablePart part, MutableAttributes attributes) {
		if (part == AttributablePart.NODE || part == AttributablePart.LINK
				|| part == CoreNodeRenderer.LOOSE_LIST_ITEM || part == CoreNodeRenderer.TIGHT_LIST_ITEM) {
			attributes.addValue(SourcePositionAttributesRendererExtension.SOURCE_OFFSET_ATTRIBUTE_NAME,
					String.valueOf(node.getStartOffset()));
			attributes.addValue(SourcePositionAttributesRendererExtension.SOURCE_LENGTH_ATTRIBUTE_NAME,
					String.valueOf(node.getEndOffset() - node.getStartOffset()));
		}
	}

	public static AttributeProviderFactory factory() {
		return new IndependentAttributeProviderFactory() {
			@Override
			public SourcePositionAttributesProvider apply(LinkResolverContext context) {
				return new SourcePositionAttributesProvider();
			}
		};
	}
}
