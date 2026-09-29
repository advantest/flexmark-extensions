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
import com.advantest.flexmark.ext.plantuml.PlantUmlFileLocation;
import com.advantest.flexmark.ext.plantuml.PlantUmlFileLocations;
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
            renderMissingPlantUmlCode(targetUrl, context, htmlWriter);
            return;
        }

        plantUmlRenderer.renderPlantUmlCode(pumlFileContents, node.getText() != null ? node.getText().toString() : null, htmlWriter, context);
    }

    /**
     * Renders, in place of a diagram, what kept it from being rendered.
     *
     * <p>Whoever parses a document says what the PlantUML code of a referenced diagram is, so
     * nothing is read here. That side also says where it looked for the file, and a reader is told
     * that place so that the reference can be checked: a file that is not there and one that is
     * there but cannot be read are two different things to go looking for.</p>
     */
    private void renderMissingPlantUmlCode(String targetUrl, NodeRendererContext context, HtmlWriter htmlWriter) {
        PlantUmlFileLocations fileLocations = PlantUmlExtension.KEY_DOCUMENT_PLANTUML_FILE_LOCATIONS.get(context.getDocument());
        PlantUmlFileLocation location = targetUrl == null || fileLocations == null
                ? null : fileLocations.locationOf(targetUrl);

        String message;
        if (location == null) {
            message = String.format("No PlantUML code available for \"%s\".", targetUrl);
        } else if (location.exists()) {
            message = String.format("Could not read PlantUML file \"%s\"", targetUrl);
        } else {
            message = String.format("PlantUML file \"%s\" (resolved path: \"%s\") does not exist.",
                    targetUrl, location.resolvedPath());
        }

        LOG.debug("No PlantUML code was handed over for \"{}\", so \"{}\" is rendered in place of"
                + " the diagram.", targetUrl, message);

        plantUmlRenderer.renderErrorMessage(message, context, htmlWriter);
    }

    public static class Factory implements NodeRendererFactory {
        @NotNull
        @Override
        public NodeRenderer apply(@NotNull DataHolder options) {
            return new PlantUmlImageNodeRenderer();
        }
    }
}
