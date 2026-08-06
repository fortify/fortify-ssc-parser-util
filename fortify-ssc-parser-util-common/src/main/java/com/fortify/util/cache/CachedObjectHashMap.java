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
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HashMap wrapper for storing CachedObject values.
 * 
 * Provides transparent access to cached objects without exposing
 * CachedObject in the collection type signature.
 * 
 * **Key principle**: Callers remain responsible for creating and putting
 * CachedObject instances. This class only simplifies the get operation.
 * 
 * @author Sangamesh Vijaykumar
 * 
 * @param <K> Key type
 * @param <V> Value type (the unwrapped object, not CachedObject)
 */
public class CachedObjectHashMap<K, V> extends HashMap<K, CachedObject<V>> {
    private static final Logger LOG = LoggerFactory.getLogger(CachedObjectHashMap.class);

    public V getCachedObject(K key) throws IOException {
        CachedObject<V> cached = get(key);
        if (cached == null) {
            LOG.debug("Input error: key {} not found", key);
            return null;
        }
        try {
            return cached.getOrReload();
        } catch (IOException e) {
            LOG.error("Failed to reload cached object with key {}", key, e);
            throw e;
        }
    }
}
