/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.math.internal;

import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.advantest.flexmark.ext.math.MathFormulaDisplayModeNode;
import com.advantest.flexmark.ext.math.MathFormulaInLineNode;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.data.DataHolder;

public class MathFormulaNodeRenderer implements NodeRenderer {

    @Override
    public @Nullable Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(MathFormulaInLineNode.class, this::render));
        set.add(new NodeRenderingHandler<>(MathFormulaDisplayModeNode.class, this::render));
        return set;
    }

    private void render(MathFormulaInLineNode node, NodeRendererContext context, HtmlWriter htmlWriter) {
        htmlWriter.srcPos(node.getChars());
        
        htmlWriter.indent().attr("class", "math inline").withAttr().tag("span");
        htmlWriter.append("\\(");
        
        htmlWriter.append(node.getText());
        
        htmlWriter.append("\\)");
        htmlWriter.tag("/span").unIndent();
    }

    private void render(MathFormulaDisplayModeNode node, NodeRendererContext context, HtmlWriter htmlWriter) {
        htmlWriter.srcPos(node.getChars());
        
        htmlWriter.tagLine("p");
        
        htmlWriter.indent().attr("class", "math display").withAttr().tag("span");
        htmlWriter.append("\\[");
        
        htmlWriter.append(node.getChildChars());
        
        htmlWriter.append("\\]");
        htmlWriter.tag("/span").unIndent();
        
        htmlWriter.tagLine("/p");
    }

    public static class Factory implements NodeRendererFactory {
        @NotNull
        @Override
        public NodeRenderer apply(@NotNull DataHolder options) {
            return new MathFormulaNodeRenderer();
        }
    }

}
