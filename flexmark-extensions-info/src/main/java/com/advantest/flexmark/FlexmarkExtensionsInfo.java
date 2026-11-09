/*
 * Copyright © 2022-2026 Advantest Europe GmbH
 * SPDX-License-Identifier: BSD-3-Clause
 */
package com.advantest.flexmark;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Tells which version of the flexmark-java extensions runs. All extensions are built and released
 * together, so they share this version.
 */
public final class FlexmarkExtensionsInfo {

    /**
     * The resource the build writes the version into. It is named after the coordinates of this
     * module, so that no other library on the class path can hide it.
     */
    static final String VERSION_RESOURCE = "META-INF/com.advantest.flexmark/flexmark-extensions-info/version.properties";

    private static final String VERSION_KEY = "version";

    private static final String VERSION = readVersion(VERSION_RESOURCE);

    private FlexmarkExtensionsInfo() {
    }

    /**
     * @return the version of the flexmark-java extensions, or {@code null} if it cannot be read
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * Reads the version a resource of this module names.
     *
     * @return the version, or {@code null} if the resource cannot be read, names no version or was
     *         not filled in by the build
     */
    static String readVersion(String resource) {
        try (InputStream stream = FlexmarkExtensionsInfo.class.getClassLoader().getResourceAsStream(resource)) {
            if (stream == null) {
                return null;
            }
            Properties properties = new Properties();
            properties.load(stream);
            String version = properties.getProperty(VERSION_KEY);
            if (version == null || version.isBlank() || version.contains("${")) {
                return null;
            }
            return version.strip();
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }
}