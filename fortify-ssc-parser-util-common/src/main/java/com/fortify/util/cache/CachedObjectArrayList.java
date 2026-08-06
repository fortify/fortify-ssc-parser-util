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
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ArrayList wrapper for storing CachedObject values.
 * 
 * Provides transparent access to cached objects without exposing
 * CachedObject in the collection type signature.
 * 
 * **Key principle**: Callers remain responsible for creating and adding
 * CachedObject instances. This class only simplifies the get operation.
 * 
 * @author Sangamesh Vijaykumar
 * 
 * @param <V> Value type (the unwrapped object, not CachedObject)
 */
public class CachedObjectArrayList<V> extends ArrayList<CachedObject<V>> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(CachedObjectArrayList.class);

    /**
     * Get object from cached list, unwrapping via getOrReload().
     * 
     * @param index Index to retrieve
     * @return Unwrapped object
     * @throws IndexOutOfBoundsException if index out of range
     * @throws IOException               if reload fails
     */
    public V getCachedObject(int index) throws IOException {
        CachedObject<V> cached = get(index);
        if (cached == null) {
            LOG.debug("Cached object is null at index {}", index);
            return null;
        }
        return cached.getOrReload();
    }
}
