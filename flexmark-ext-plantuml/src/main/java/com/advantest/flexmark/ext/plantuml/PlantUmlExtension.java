/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.plantuml;

import com.advantest.flexmark.ext.plantuml.internal.FencedCodeBlockReplacingPostProcessor;
import com.advantest.flexmark.ext.plantuml.internal.ImageReplacingPostProcessor;
import com.advantest.flexmark.ext.plantuml.internal.PlantUmlBlockNodeRenderer;
import com.advantest.flexmark.ext.plantuml.internal.PlantUmlCodeBlockParser;
import com.advantest.flexmark.ext.plantuml.internal.PlantUmlFencedCodeBlockRenderer;
import com.advantest.flexmark.ext.plantuml.internal.PlantUmlImageNodeRenderer;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.DataKey;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.NullableDataKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import net.sourceforge.plantuml.error.PSystemError;

public class PlantUmlExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    public static final NullableDataKey<Map<String,String>> KEY_DOCUMENT_PATH_TO_FILE_CONTENTS_MAP = new NullableDataKey<>("RELATIVE_PATH_TO_FILE_CONTENTS_MAP");

    /**
     * Where the PlantUML files a document refers to were looked for, asked about a reference that
     * produced no code. A document that does not carry this is rendered as before, only without
     * the place in the message saying that a diagram is missing.
     */
    public static final NullableDataKey<PlantUmlFileLocations> KEY_DOCUMENT_PLANTUML_FILE_LOCATIONS = new NullableDataKey<>("PLANTUML_FILE_LOCATIONS");

    public static final NullableDataKey<Boolean> KEY_RENDER_FENCED_PLANTUML_CODE_BLOCKS = new NullableDataKey<Boolean>("RENDER_FENCED_PLANUML_CODE_BLOCKS", false);

    /**
     * Whether diagrams are rendered for a dark background, using PlantUML's own dark mode. The
     * PlantUML code is left as it is. It is read from the document when a diagram is rendered, so
     * it can be set as an option or on a single document, which then overrides the options.
     */
    public static final DataKey<Boolean> KEY_DARK_MODE = new DataKey<>("DARK_MODE", false);

    private PlantUmlExtension() {
    }

    public static PlantUmlExtension create() {
        return new PlantUmlExtension();
    }

    /**
     * Switches off the decorations PlantUML adds to its error diagrams depending on the time, such as requests for
     * donations that change from minute to minute. The same diagram then renders the same way every time; the error
     * message itself stays. The extension's renderers call this before they draw a diagram, so users of the extension
     * need not; code that runs PlantUML itself should. It only sets a flag of PlantUML's, so any thread may call it any
     * number of times.
     */
    public static void disableTimeBasedErrorDecorations() {
        PSystemError.disableTimeBasedErrorDecorations();
    }

    @Override
    public void parserOptions(MutableDataHolder options) {

    }

    @Override
    public void rendererOptions(@NotNull MutableDataHolder options) {

    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new PlantUmlCodeBlockParser.Factory())
            .postProcessorFactory(new ImageReplacingPostProcessor.Factory(parserBuilder))
            .postProcessorFactory(new FencedCodeBlockReplacingPostProcessor.Factory(parserBuilder));
    }

    @Override
    public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
        if (htmlRendererBuilder.isRendererType("HTML")) {
            htmlRendererBuilder
                    .nodeRendererFactory(new PlantUmlBlockNodeRenderer.Factory())
                    .nodeRendererFactory(new PlantUmlImageNodeRenderer.Factory())
                    .nodeRendererFactory(new PlantUmlFencedCodeBlockRenderer.Factory());
        }
    }

}
