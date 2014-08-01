/*
 * Copyright (C) 2014 SCVNGR, Inc. d/b/a LevelUp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.scvngr.levelup.core.util;

import android.support.annotation.NonNull;

import com.scvngr.levelup.core.util.ContextInstrumenter.Provider;

/**
 * This class allows instrumentation tests to inject mocked or wrapped Activity contexts. This class
 * simply provides access to the package-private members of {@link ContextInstrumenter}. Test cases
 * that set a context instrumentation provider using {@link #set} should call {@link #clear} from the {@code setUp} and
 * {@code tearDown} methods.
 */
public final class ContextInstrumenterHelper {

    /**
     * Clear the {@link ContextInstrumenter} provider.
     */
    public static void clear() {
        ContextInstrumenter.mProvider.set(null);
    }

    /**
     * Set the {@link ContextInstrumenter} provider.
     *
     * @param provider the {@link Provider}.
     */
    public static void set(@NonNull final Provider provider) {
        ContextInstrumenter.mProvider.set(provider);
    }

    /**
     * Private constructor prevents instantiation.
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private ContextInstrumenterHelper() {
        throw new UnsupportedOperationException("This class is non-instantiable");
    }
}
