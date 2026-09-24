/*
 * This work is made available under the terms of the BSD 2-Clause "Simplified" License.
 * The BSD accompanies this distribution (LICENSE.txt).
 * 
 * Copyright © 2022-2024 Advantest Europe GmbH. All rights reserved.
 */
package com.advantest.flexmark.ext.plantuml.internal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.advantest.flexmark.ext.plantuml.PlantUmlExtension;
import com.advantest.flexmark.ext.plantuml.PlantUmlImage;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.data.DataHolder;

public class PlantUmlImageNodeRenderer implements NodeRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(PlantUmlImageNodeRenderer.class);

    private PlantUmlBlockNodeRenderer plantUmlRenderer = new PlantUmlBlockNodeRenderer();
    
    @Override
    public @Nullable Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(PlantUmlImage.class, this::render));
        return set;
    }

    private void render(PlantUmlImage node, NodeRendererContext context, HtmlWriter htmlWriter) {
        Map<String, String> referencedFilesContents = PlantUmlExtension.KEY_DOCUMENT_PATH_TO_FILE_CONTENTS_MAP.get(context.getDocument());
        String targetUrl = node.getUrl() != null ? node.getUrl().toString() : null;

        String pumlFileContents = null;
        if (targetUrl != null && referencedFilesContents != null) {
            pumlFileContents = referencedFilesContents.get(targetUrl);
        }

        if (pumlFileContents == null) {
            // Whoever parses a document says what the PlantUML code of a referenced diagram is,
            // so nothing is read here and a reference nobody answered renders as a message.
            LOG.debug("No PlantUML code was handed over for \"{}\", so the message saying so is"
                    + " rendered in place of the diagram.", targetUrl);

            plantUmlRenderer.renderErrorMessage(String.format(
                    "No PlantUML code available for \"%s\".", targetUrl), context, htmlWriter);
            return;
        }

        plantUmlRenderer.renderPlantUmlCode(pumlFileContents, node.getText() != null ? node.getText().toString() : null, htmlWriter, context);
    }

    public static class Factory implements NodeRendererFactory {
        @NotNull
        @Override
        public NodeRenderer apply(@NotNull DataHolder options) {
            return new PlantUmlImageNodeRenderer();
        }
    }
}
