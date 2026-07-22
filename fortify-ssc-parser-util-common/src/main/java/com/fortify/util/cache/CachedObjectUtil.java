/*******************************************************************************
 * (c) Copyright 2020 Micro Focus or one of its affiliates
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to 
 * whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.fortify.util.cache;

import java.io.IOException;

import org.slf4j.Logger;

/**
 * Utility methods for working with CachedObject collections.
 * 
 * Reduces boilerplate in getter methods by centralizing:
 * - Null checks
 * - Bounds checks
 * - Exception handling
 * - Logging
 */
public final class CachedObjectUtil {

    private CachedObjectUtil() {
    } // Static utility class

    /**
     * Get object from cached list with full error handling.
     * 
     * **Usage in getters:**
     * ```java
     * public Artifact getArtifactByIndex(Integer index) {
     * return CachedObjectUtil.getOrNull(artifactsByIndex, index, LOG, "artifact");
     * }
     * ```
     * 
     * @param <T>        Object type
     * @param list       List of CachedObject
     * @param index      Index to retrieve
     * @param logger     Logger for warnings/errors
     * @param objectName Name of object type (for logging: "artifact", "rule", etc.)
     * @return Object if found and unwrapped, null otherwise
     */
    public static <T> T getOrNull(java.util.List<CachedObject<T>> list, Integer index,
            Logger logger, String objectName) {
        // Null or empty check
        if (index == null || list == null || list.isEmpty()) {
            return null;
        }

        // Bounds check
        if (index < 0 || index >= list.size()) {
            logger.warn("Input error: Invalid {} index {}", objectName, index);
            return null;
        }

        // Unwrap CachedObject
        try {
            CachedObject<T> cached = list.get(index);
            return cached.getOrReload();
        } catch (IOException e) {
            logger.error("Failed to reload {} at index {}", objectName, index, e);
            return null;
        }
    }
}
