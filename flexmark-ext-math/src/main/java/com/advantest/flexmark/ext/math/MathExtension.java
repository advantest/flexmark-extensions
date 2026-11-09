/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark.ext.math;

import com.advantest.flexmark.ext.math.internal.MathFormulaBlockParser;
import com.advantest.flexmark.ext.math.internal.MathFormulaInLineDelimiterProcessor;
import com.advantest.flexmark.ext.math.internal.MathFormulaNodeRenderer;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import org.jetbrains.annotations.NotNull;


public class MathExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    private MathExtension() {
    }

    public static MathExtension create() {
        return new MathExtension();
    }

    @Override
    public void parserOptions(MutableDataHolder options) {

    }

    @Override
    public void rendererOptions(@NotNull MutableDataHolder options) {

    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new MathFormulaBlockParser.Factory());
        parserBuilder.customDelimiterProcessor(new MathFormulaInLineDelimiterProcessor());
    }

    @Override
    public void extend(@NotNull HtmlRenderer.Builder htmlRendererBuilder, @NotNull String rendererType) {
        if (htmlRendererBuilder.isRendererType("HTML")) {
            htmlRendererBuilder.nodeRendererFactory(new MathFormulaNodeRenderer.Factory());
        }
    }

}
