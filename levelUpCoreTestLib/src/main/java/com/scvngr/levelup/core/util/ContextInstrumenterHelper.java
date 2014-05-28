/*
 * Copyright 2013-2014 SCVNGR, Inc., D.B.A. LevelUp. All rights reserved.
 */
package com.scvngr.levelup.core.util;

import com.scvngr.levelup.core.annotation.NonNull;
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
